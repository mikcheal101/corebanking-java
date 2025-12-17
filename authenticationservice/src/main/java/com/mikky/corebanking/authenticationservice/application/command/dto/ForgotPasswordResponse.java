package com.mikky.corebanking.authenticationservice.application.command.dto;

import lombok.Getter;

@Getter
public class ForgotPasswordResponse extends ApiResponse {

    private final String token;

    public ForgotPasswordResponse(String message, String token) {
        super(message);
        this.token = token;
    }
}
