package com.mikky.corebanking.authenticationservice.command.dto;

import lombok.Getter;

@Getter
public class SigninResponse extends ApiResponse {

    private final String token;

    public SigninResponse(String message, String token) {
        super(message);
        this.token = token;
    }
}
