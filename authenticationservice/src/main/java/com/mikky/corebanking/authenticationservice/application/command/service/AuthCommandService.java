package com.mikky.corebanking.authenticationservice.application.command.service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mikky.corebanking.authenticationservice.application.command.dto.ForgotPasswordChangeRequest;
import com.mikky.corebanking.authenticationservice.application.command.dto.ForgotPasswordRequest;
import com.mikky.corebanking.authenticationservice.application.command.dto.SigninRequest;
import com.mikky.corebanking.authenticationservice.application.command.dto.SignupRequest;
import com.mikky.corebanking.authenticationservice.domain.exceptions.PasswordTokenExpiredException;
import com.mikky.corebanking.authenticationservice.domain.exceptions.PasswordTokenInvalidException;
import com.mikky.corebanking.authenticationservice.domain.exceptions.PasswordTokenNotFoundException;
import com.mikky.corebanking.authenticationservice.domain.exceptions.RoleNotFoundException;
import com.mikky.corebanking.authenticationservice.domain.exceptions.UserDoesNotExistException;
import com.mikky.corebanking.authenticationservice.domain.exceptions.UsernameAlreadyExistsException;
import com.mikky.corebanking.authenticationservice.domain.model.PasswordResetToken;
import com.mikky.corebanking.authenticationservice.domain.model.Permission;
import com.mikky.corebanking.authenticationservice.domain.model.Role;
import com.mikky.corebanking.authenticationservice.domain.model.User;
import com.mikky.corebanking.authenticationservice.infrastructure.messaging.kafka.publisher.ForgotPasswordChangeEventPublisher;
import com.mikky.corebanking.authenticationservice.infrastructure.messaging.kafka.publisher.ForgotPasswordEventPublisher;
import com.mikky.corebanking.authenticationservice.infrastructure.messaging.kafka.publisher.UserCreatedEventPublisher;
import com.mikky.corebanking.authenticationservice.infrastructure.messaging.kafka.publisher.UserSignedInEventPublisher;
import com.mikky.corebanking.authenticationservice.infrastructure.persistence.command.PasswordResetTokenCommandRepository;
import com.mikky.corebanking.authenticationservice.infrastructure.persistence.command.PermissionCommandRepository;
import com.mikky.corebanking.authenticationservice.infrastructure.persistence.command.RoleCommandRepository;
import com.mikky.corebanking.authenticationservice.infrastructure.persistence.command.UserCommandRepository;
import com.mikky.corebanking.authenticationservice.infrastructure.persistence.query.PasswordResetTokenQueryRepository;
import com.mikky.corebanking.authenticationservice.infrastructure.persistence.query.RoleQueryRepository;
import com.mikky.corebanking.authenticationservice.infrastructure.persistence.query.UserQueryRepository;
import com.mikky.corebanking.authenticationservice.shared.util.FormattingUtility;
import com.mikky.corebanking.authenticationservice.shared.util.IJwtUtility;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordChangeEvent;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordEvent;
import com.mikky.corebanking.events.domain.event.auth.UserCreatedEvent;
import com.mikky.corebanking.events.domain.event.auth.UserSignedInEvent;
import com.mikky.corebanking.events.domain.event.notification.Channel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthCommandService {

    private final UserCommandRepository userCommandRepository;
    private final UserQueryRepository userQueryRepository;
    private final RoleQueryRepository roleQueryRepository;
    private final RoleCommandRepository roleCommandRepository;
    private final PermissionCommandRepository permissionCommandRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenCommandRepository passwordResetTokenCommandRepository;
    private final PasswordResetTokenQueryRepository passwordResetTokenQueryRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ForgotPasswordChangeEventPublisher forgotPasswordChangeEventPublisher;
    private final ForgotPasswordEventPublisher forgotPasswordEventPublisher;
    private final UserSignedInEventPublisher signedInEventPublisher;
    private final UserCreatedEventPublisher signedUpEventPublisher;

    @Transactional
    public void registerUser(SignupRequest signupRequest) throws UsernameAlreadyExistsException, RoleNotFoundException {
        // check for user existence
        if (userQueryRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UsernameAlreadyExistsException(signupRequest.getUsername());
        }

        // create new user
        var user = new User();
        user.setUsername(signupRequest.getUsername());

        var passcode = passwordEncoder.encode(signupRequest.getPassword());
        user.setPassword(passcode);

        var role = roleQueryRepository.findByName(FormattingUtility.formatRoleName("CUSTOMER_B2C"))
                .orElseThrow(() -> new RoleNotFoundException(FormattingUtility.formatRoleName("CUSTOMER_B2C")));
        user.setRoles(Set.of(role));

        // save user
        userCommandRepository.save(user);

        var event = UserCreatedEvent.builder()
                .eventType(EventType.USER_CREATED)
                .occurredAt(Instant.now())
                .version(1)
                .payload(
                        UserCreatedEvent.Payload.builder()
                                .channels(Set.of(Channel.EMAIL))
                                .username(user.getUsername())
                                .expiry(3200)
                                .build())
                .build();

        try {
            signedUpEventPublisher.publish(event);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Transactional
    public User loginUser(SigninRequest signinRequest) throws UserDoesNotExistException {
        // check if user exists
        User user = userQueryRepository
                .findByUsername(signinRequest.getUsername())
                .orElseThrow(() -> new UserDoesNotExistException(signinRequest.getUsername()));

        // check password
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new UserDoesNotExistException(signinRequest.getUsername());
        }

        var event = UserSignedInEvent.builder()
                .eventType(EventType.USER_LOGGED_IN)
                .occurredAt(Instant.now())
                .version(1)
                .payload(
                        UserSignedInEvent.Payload.builder()
                                .channels(Set.of(Channel.EMAIL))
                                .expiry(3200)
                                .username(user.getUsername())
                                .build())
                .build();

        try {
            signedInEventPublisher.publish(event);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return user;
    }

    @Transactional
    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws UserDoesNotExistException {
        User user = userQueryRepository
                .findByUsername(forgotPasswordRequest.getUsername())
                .orElseThrow(() -> new UserDoesNotExistException(forgotPasswordRequest.getUsername()));

        String token = UUID.randomUUID().toString() + UUID.randomUUID().toString();

        var passwordReset = new PasswordResetToken();
        passwordReset.setOwner(user);
        passwordReset.setToken(token);
        passwordResetTokenCommandRepository.save(passwordReset);

        // Prepare the event
        var forgotPasswordEvent = ForgotPasswordEvent.builder()
                .eventType(EventType.FORGOT_PASSWORD)
                .version(1)
                .occurredAt(Instant.now())
                .payload(
                        ForgotPasswordEvent.Payload.builder()
                                .changedAt(Instant.now())
                                .channels(Set.of(Channel.EMAIL))
                                .expiry(3200) // 3200 secs after now
                                .resetToken(token)
                                .username(user.getUsername())
                                .build())
                .build();

        try {
            forgotPasswordEventPublisher.publish(forgotPasswordEvent);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return token;
    }

    @Transactional
    public void changeForgottenPassword(String token, ForgotPasswordChangeRequest forgotPasswordChangeRequest)
            throws PasswordTokenNotFoundException, PasswordTokenExpiredException, PasswordTokenInvalidException {
        // validate the token sent
        var passwordToken = passwordResetTokenQueryRepository
                .findByToken(token)
                .orElseThrow(PasswordTokenNotFoundException::new);

        if (Instant.now().compareTo(passwordToken.getExpiry()) <= 0) {
            // invalidate the token
            passwordToken.setValid(false);
            passwordResetTokenCommandRepository.save(passwordToken);
            throw new PasswordTokenExpiredException();
        }

        if (!passwordToken.isValid()) {
            throw new PasswordTokenInvalidException();
        }

        // change the user's password
        User user = passwordToken.getOwner();
        String newPassword = passwordEncoder.encode(forgotPasswordChangeRequest.getPassword());
        user.setPassword(newPassword);
        userCommandRepository.save(user);

        // invalidate token
        passwordToken.setValid(false);
        passwordResetTokenCommandRepository.save(passwordToken);

        // send notification
        var event = ForgotPasswordChangeEvent.builder()
                .eventType(EventType.FORGOT_PASSWORD_CHANGED)
                .occurredAt(Instant.now())
                .version(1)
                .payload(
                        ForgotPasswordChangeEvent.Payload.builder()
                                .expiry(3200)
                                .username(user.getUsername())
                                .channels(Set.of(Channel.EMAIL, Channel.PUSH, Channel.SMS, Channel.WHATSAPP))
                                .build())
                .build();

        try {
            forgotPasswordChangeEventPublisher.publish(event);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public boolean createRole(String roleName) {
        try {
            var role = Role.builder().name(FormattingUtility.formatRoleName(roleName)).build();
            this.roleCommandRepository.save(role);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean createPermission(String permissionName, String roleName) throws RoleNotFoundException {
        try {
            // get the role
            var role = this.roleQueryRepository.findByName(FormattingUtility.formatRoleName(roleName))
                    .orElseThrow(() -> new RoleNotFoundException(FormattingUtility.formatRoleName(roleName)));
            var permission = Permission.builder().name(FormattingUtility.formatPermissionName(permissionName)).build();
            this.permissionCommandRepository.save(permission);

            role.getPermissions().add(permission);
            this.roleCommandRepository.save(role);
            return true;
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            return false;
        }
    }

}
