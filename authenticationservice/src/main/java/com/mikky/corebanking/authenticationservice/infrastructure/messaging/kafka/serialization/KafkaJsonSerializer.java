package com.mikky.corebanking.authenticationservice.infrastructure.messaging.kafka.serialization;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class KafkaJsonSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper;

    public KafkaJsonSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }

        try {
            return this.objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException(e.getMessage());
        }
    }
    
}
