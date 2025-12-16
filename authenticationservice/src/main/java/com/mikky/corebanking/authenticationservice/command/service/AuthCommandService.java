package com.mikky.corebanking.authenticationservice.command.service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mikky.corebanking.authenticationservice.command.dto.ForgotPasswordChangeRequest;
import com.mikky.corebanking.authenticationservice.command.dto.ForgotPasswordRequest;
import com.mikky.corebanking.authenticationservice.command.dto.SigninRequest;
import com.mikky.corebanking.authenticationservice.command.dto.SignupRequest;
import com.mikky.corebanking.authenticationservice.command.repository.PasswordResetTokenCommandRepository;
import com.mikky.corebanking.authenticationservice.command.repository.UserCommandRepository;
import com.mikky.corebanking.authenticationservice.domain.event.Channel;
import com.mikky.corebanking.authenticationservice.domain.event.EventType;
import com.mikky.corebanking.authenticationservice.domain.event.ForgotPasswordChangeEvent;
import com.mikky.corebanking.authenticationservice.domain.event.ForgotPasswordEvent;
import com.mikky.corebanking.authenticationservice.domain.event.UserCreatedEvent;
import com.mikky.corebanking.authenticationservice.domain.event.UserSignedInEvent;
import com.mikky.corebanking.authenticationservice.domain.exceptions.PasswordTokenExpiredException;
import com.mikky.corebanking.authenticationservice.domain.exceptions.PasswordTokenInvalidException;
import com.mikky.corebanking.authenticationservice.domain.exceptions.PasswordTokenNotFoundException;
import com.mikky.corebanking.authenticationservice.domain.exceptions.RoleNotFoundException;
import com.mikky.corebanking.authenticationservice.domain.exceptions.UserDoesNotExistException;
import com.mikky.corebanking.authenticationservice.domain.exceptions.UsernameAlreadyExistsException;
import com.mikky.corebanking.authenticationservice.domain.model.PasswordResetToken;
import com.mikky.corebanking.authenticationservice.domain.model.RoleType;
import com.mikky.corebanking.authenticationservice.domain.model.User;
import com.mikky.corebanking.authenticationservice.messaging.publisher.ForgotPasswordChangeEventPublisher;
import com.mikky.corebanking.authenticationservice.messaging.publisher.ForgotPasswordEventPublisher;
import com.mikky.corebanking.authenticationservice.messaging.publisher.UserCreatedEventPublisher;
import com.mikky.corebanking.authenticationservice.messaging.publisher.UserSignedInEventPublisher;
import com.mikky.corebanking.authenticationservice.query.repository.PasswordResetTokenQueryRepository;
import com.mikky.corebanking.authenticationservice.query.repository.RoleQueryRepository;
import com.mikky.corebanking.authenticationservice.query.repository.UserQueryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthCommandService {

    private final UserCommandRepository userCommandRepository;
    private final UserQueryRepository userQueryRepository;
    private final RoleQueryRepository roleQueryRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenCommandRepository passwordResetTokenCommandRepository;
    private final PasswordResetTokenQueryRepository passwordResetTokenQueryRepository;

    private final ForgotPasswordChangeEventPublisher forgotPasswordChangeEventPublisher;
    private final ForgotPasswordEventPublisher forgotPasswordEventPublisher;
    private final UserSignedInEventPublisher signedInEventPublisher;
    private final UserCreatedEventPublisher signedUpEventPublisher;

    @Transactional
    public void registerUser(SignupRequest signupRequest) {
        // check for user existence
        if (userQueryRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UsernameAlreadyExistsException(signupRequest.getUsername());
        }

        // create new user
        var user = new User();
        user.setUsername(signupRequest.getUsername());

        var passcode = passwordEncoder.encode(signupRequest.getPassword());
        user.setPassword(passcode);

        var role = roleQueryRepository.findByRoleType(RoleType.CUSTOMER)
                .orElseThrow(() -> new RoleNotFoundException(RoleType.CUSTOMER));
        user.setRoles(Set.of(role));

        // save user
        userCommandRepository.save(user);

        var event = UserCreatedEvent.builder()
                .eventType(EventType.USER_CREATED)
                .occurredAt(Instant.now())
                .version(1)
                .payload(
                        UserCreatedEvent.Payload.builder()
                                .channels(Set.of(Channel.SMS, Channel.EMAIL))
                                .username(user.getUsername())
                                .expiry(3200)
                                .build())
                .build();

        signedUpEventPublisher.publish(event);
    }

    @Transactional
    public User loginUser(SigninRequest signinRequest) {
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
                                .channels(Set.of(Channel.EMAIL, Channel.SMS, Channel.WHATSAPP))
                                .expiry(3200)
                                .username(user.getUsername())
                                .build())
                .build();

        signedInEventPublisher.publish(event);
        return user;
    }

    @Transactional
    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
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

        forgotPasswordEventPublisher.publish(forgotPasswordEvent);
        return token;
    }

    @Transactional
    public void changeForgottenPassword(String token, ForgotPasswordChangeRequest forgotPasswordChangeRequest) {
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
            .occuredAt(Instant.now())
            .version(1)
            .payload(
                ForgotPasswordChangeEvent.Payload.builder()
                    .expiry(3200)
                    .username(user.getUsername())
                    .channels(Set.of(Channel.EMAIL, Channel.PUSH, Channel.SMS, Channel.WHATSAPP))
                    .build()
            )
            .build();
        
        forgotPasswordChangeEventPublisher.publish(event);
    }
}
