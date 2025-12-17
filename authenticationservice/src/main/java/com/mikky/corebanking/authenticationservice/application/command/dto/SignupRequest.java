package com.mikky.corebanking.authenticationservice.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank(message = "Username is required!")
    @Size(min = 10, max = 50, message = "Username length must be greater than 10.")
    private String username;

    @NotBlank(message = "Password is required!")
    @Size(min = 10, max = 20, message = "Password length must be greater than 10!.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least one uppercase and one special character!")
    private String password;
}
