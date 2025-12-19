package com.mikky.corebanking.authenticationservice.domain.exceptions;

import com.mikky.corebanking.events.domain.exceptions.CustomException;

public class PasswordTokenInvalidException extends CustomException {
    public PasswordTokenInvalidException() {
        super("Password Reset Token Invalid!");
    }
}
