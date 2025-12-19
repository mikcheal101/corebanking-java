package com.mikky.corebanking.authenticationservice.infrastructure.messaging.kafka;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class KafkaEventEnvelope<T> {
    private String eventId;
    private String eventType;
    private int version;
    private Instant occuredAt;
    private T payload;

    public KafkaEventEnvelope(String eventType, T payload) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.version = 1;
        this.occuredAt = Instant.now();
        this.payload = payload;
    }
}
