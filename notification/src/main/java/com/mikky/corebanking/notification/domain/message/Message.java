package com.mikky.corebanking.notification.domain.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;

@Getter
public abstract class Message {

    private String to;
    private String from;
    private String body;
    private MessageType messageType;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public Message setTo(String to) throws IllegalArgumentException {
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Message Recipient required!");
        }

        this.to = to;
        return this;
    }

    public Message setFrom(String from) throws IllegalArgumentException {
        if (from == null || from.isEmpty()) {
            throw new IllegalArgumentException("Message Sender is required!");
        }

        this.from = from;
        return this;
    }

    public Message setBody(String body) throws IllegalArgumentException {
        if (body == null || body.isEmpty()) {
            throw new IllegalArgumentException("Message Body/Content is required!");
        }

        this.body = body;
        return this;
    }

    public Message setMessageType(MessageType messageType) {
        this.messageType = messageType == null ? MessageType.PLAINTEXT : messageType;
        return this;
    }
}
