package com.mikky.corebanking.notification.domain.exceptions;

public class MessageTemplateNotFoundException extends RuntimeException {

    public MessageTemplateNotFoundException() {
        super("Error: Message Template not found!");
    }
}
