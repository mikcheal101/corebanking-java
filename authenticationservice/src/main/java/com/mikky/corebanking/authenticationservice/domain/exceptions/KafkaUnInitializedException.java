package com.mikky.corebanking.authenticationservice.domain.exceptions;

public class KafkaUnInitializedException extends RuntimeException {

    public KafkaUnInitializedException() {
        super("Kafka not initialized!");
    }
}
