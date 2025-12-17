package com.mikky.corebanking.authenticationservice.infrastructure.messaging.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.authenticationservice.domain.event.UserCreatedEvent;

@Component
public class UserCreatedEventPublisher extends KafkaEventPublisher<UserCreatedEvent> {

    public UserCreatedEventPublisher(KafkaTemplate<String, UserCreatedEvent> template) {
        this.setKafkaTemplate(template);
    }
}
