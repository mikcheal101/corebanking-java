package com.mikky.corebanking.authenticationservice.domain.exceptions;

public class PasswordTokenNotFoundException extends CustomException {
    public PasswordTokenNotFoundException() {
        super("Password Reset Token not Found!");
    }
}
