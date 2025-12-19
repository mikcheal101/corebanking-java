package com.mikky.corebanking.authenticationservice.domain.exceptions;

import com.mikky.corebanking.events.domain.exceptions.CustomException;

public class PasswordTokenExpiredException extends CustomException {
    public PasswordTokenExpiredException() {
        super("Password Reset Token Expired!");
    }
}
