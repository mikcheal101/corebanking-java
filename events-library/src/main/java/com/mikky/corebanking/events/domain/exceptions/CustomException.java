package com.mikky.corebanking.events.domain.exceptions;

public abstract class CustomException extends RuntimeException {

    protected CustomException(String message) {
        super("Error: " + message);
    }
}
