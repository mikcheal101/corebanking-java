package com.mikky.corebanking.authenticationservice.api;

import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mikky.corebanking.authenticationservice.command.dto.ForgotPasswordChangeRequest;
import com.mikky.corebanking.authenticationservice.command.dto.ForgotPasswordChangeResponse;
import com.mikky.corebanking.authenticationservice.command.dto.ForgotPasswordRequest;
import com.mikky.corebanking.authenticationservice.command.dto.ForgotPasswordResponse;
import com.mikky.corebanking.authenticationservice.command.dto.SigninRequest;
import com.mikky.corebanking.authenticationservice.command.dto.SigninResponse;
import com.mikky.corebanking.authenticationservice.command.dto.SignupRequest;
import com.mikky.corebanking.authenticationservice.command.dto.SignupResponse;
import com.mikky.corebanking.authenticationservice.command.service.AuthCommandService;
import com.mikky.corebanking.authenticationservice.command.service.TokenBlacklistCommandService;
import com.mikky.corebanking.authenticationservice.domain.model.User;
import com.mikky.corebanking.authenticationservice.security.CustomUserDetailsService;
import com.mikky.corebanking.authenticationservice.shared.util.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthCommandService authCommandService;
    private final TokenBlacklistCommandService tokenBlacklistCommandService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            authCommandService.registerUser(signupRequest);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SignupResponse("User created successfully"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new SignupResponse(e.getMessage()));
        }
    }

    @GetMapping("/signout")
    public ResponseEntity<String> signOutUser(@RequestHeader("Authorization") String authHeader) {
        String prefix = "Bearer ";
        if ((authHeader != null) && (authHeader.startsWith(prefix))) {
            String token = authHeader.substring(prefix.length());
            Instant blacklistedInstant = Instant.now();
            tokenBlacklistCommandService.blackListToken(token, blacklistedInstant);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("User Signed out Successfully!");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid request!");
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signInUser(@Valid @RequestBody SigninRequest signinRequest) {
        try {
            User user = authCommandService.loginUser(signinRequest);
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String token = jwtUtils.generateJwtToken(userDetails);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SigninResponse("successfully logged in", token));
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new SigninResponse(exception.getMessage(), null));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            String token = authCommandService.forgotPassword(forgotPasswordRequest);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ForgotPasswordResponse("Change Password Email sent, kindly verify.", token));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ForgotPasswordResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/change-forgotten-password/{token}")
    public ResponseEntity<ForgotPasswordChangeResponse> changeForgottenPassword(
            @PathVariable String token,
            @Valid @RequestBody ForgotPasswordChangeRequest forgotPasswordChangeRequest) {
        try {
            authCommandService.changeForgottenPassword(token, forgotPasswordChangeRequest);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ForgotPasswordChangeResponse("Password changed successfully!"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ForgotPasswordChangeResponse(e.getMessage()));
        }
    }
}
