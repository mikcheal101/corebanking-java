package com.mikky.corebanking.authenticationservice.infrastructure.messaging.kafka;

import java.time.Instant;

import lombok.Data;

@Data
public class KafkaEventEnvelope<T> {
    private String eventId;
    private String eventType;
    private int version;
    private Instant occuredAt;
    private T payload;
}
