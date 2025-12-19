package com.mikky.corebanking.events.domain.exceptions;

public class KafkaUnInitializedException extends CustomException {

    public KafkaUnInitializedException(String message) {
        super(message);
    }

    public KafkaUnInitializedException() {
        super("Kafka not initialized!");
    }
}
