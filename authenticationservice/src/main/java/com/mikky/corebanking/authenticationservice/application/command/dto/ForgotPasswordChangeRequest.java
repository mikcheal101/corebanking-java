package com.mikky.corebanking.authenticationservice.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordChangeRequest {

    @NotBlank(message = "Enter new Password!")
    private String password;

    @NotBlank(message = "Reenter Password")
    private String retypePassword;
}
