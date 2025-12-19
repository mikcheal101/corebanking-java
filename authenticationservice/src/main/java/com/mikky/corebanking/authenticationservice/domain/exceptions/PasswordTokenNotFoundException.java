package com.mikky.corebanking.authenticationservice.domain.exceptions;

import com.mikky.corebanking.events.domain.exceptions.CustomException;

public class PasswordTokenNotFoundException extends CustomException {
    public PasswordTokenNotFoundException() {
        super("Password Reset Token not Found!");
    }
}
