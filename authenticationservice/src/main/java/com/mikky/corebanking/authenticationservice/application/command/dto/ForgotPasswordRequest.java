package com.mikky.corebanking.authenticationservice.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {

    @NotBlank(message = "Username is required!")
    private String username;
}
