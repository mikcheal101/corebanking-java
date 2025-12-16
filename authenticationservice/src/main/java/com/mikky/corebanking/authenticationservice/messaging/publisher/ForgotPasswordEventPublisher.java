package com.mikky.corebanking.authenticationservice.messaging.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ForgotPasswordEventPublisher extends KafkaEventPublisher {
    public ForgotPasswordEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.setKafkaTemplate(kafkaTemplate);
    }
}
