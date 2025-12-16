package com.mikky.corebanking.authenticationservice.domain.exceptions;

public class PasswordTokenInvalidException extends CustomException {
    public PasswordTokenInvalidException() {
        super("Password Reset Token Invalid!");
    }
}
