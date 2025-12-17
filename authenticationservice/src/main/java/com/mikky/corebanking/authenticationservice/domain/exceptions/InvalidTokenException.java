package com.mikky.corebanking.authenticationservice.domain.exceptions;

public class InvalidTokenException extends CustomException {
    public InvalidTokenException() {
        super("Invalid Token!.");
    }
}
