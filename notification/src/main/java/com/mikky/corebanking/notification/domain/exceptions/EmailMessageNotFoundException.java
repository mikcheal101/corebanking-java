package com.mikky.corebanking.notification.domain.exceptions;

public class EmailMessageNotFoundException extends IllegalStateException {

    public EmailMessageNotFoundException() {
        super("Email Message not found!");
    }
}
