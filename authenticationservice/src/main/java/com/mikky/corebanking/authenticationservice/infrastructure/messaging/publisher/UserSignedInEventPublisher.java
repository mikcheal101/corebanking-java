package com.mikky.corebanking.authenticationservice.infrastructure.messaging.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.auth.UserSignedInEvent;

@Component
public class UserSignedInEventPublisher extends KafkaEventPublisher<UserSignedInEvent> {
    public UserSignedInEventPublisher(KafkaTemplate<String, UserSignedInEvent> template) {
        this.setKafkaTemplate(template);
    }
}
