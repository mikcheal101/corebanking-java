package com.mikky.corebanking.authenticationservice.infrastructure.messaging.kafka.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.domain.event.auth.UserCreatedEvent;
import com.mikky.corebanking.events.infrastructure.messaging.publisher.KafkaEventPublisher;

@Component
public class UserCreatedEventPublisher extends KafkaEventPublisher<UserCreatedEvent> {

    public UserCreatedEventPublisher(KafkaTemplate<String, UserCreatedEvent> template) {
        this.setKafkaTemplate(template);
    }
}
