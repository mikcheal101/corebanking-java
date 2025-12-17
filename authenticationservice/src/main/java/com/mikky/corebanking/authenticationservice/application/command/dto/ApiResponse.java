package com.mikky.corebanking.authenticationservice.application.command.dto;

import lombok.Getter;

@Getter
public abstract class ApiResponse {
    private final String message;

    protected ApiResponse(String message) {
        this.message = message;
    }
}
