package com.mikky.corebanking.authenticationservice.messaging.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventPublisher extends KafkaEventPublisher {
    public UserCreatedEventPublisher(KafkaTemplate<String, Object> template) {
        this.setKafkaTemplate(template);
    }
}
