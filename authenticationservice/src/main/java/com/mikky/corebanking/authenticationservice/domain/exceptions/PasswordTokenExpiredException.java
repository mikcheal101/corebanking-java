package com.mikky.corebanking.authenticationservice.domain.exceptions;

public class PasswordTokenExpiredException extends CustomException {
    public PasswordTokenExpiredException() {
        super("Password Reset Token Expired!");
    }
}
