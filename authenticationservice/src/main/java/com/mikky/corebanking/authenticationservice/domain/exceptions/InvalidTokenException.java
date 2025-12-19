package com.mikky.corebanking.authenticationservice.domain.exceptions;

import com.mikky.corebanking.events.domain.exceptions.CustomException;

public class InvalidTokenException extends CustomException {
    public InvalidTokenException() {
        super("Invalid Token!.");
    }
}
