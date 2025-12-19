package com.mikky.corebanking.authenticationservice.infrastructure.messaging.kafka.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.domain.event.auth.UserSignedInEvent;
import com.mikky.corebanking.events.infrastructure.messaging.publisher.KafkaEventPublisher;

@Component
public class UserSignedInEventPublisher extends KafkaEventPublisher<UserSignedInEvent> {
    public UserSignedInEventPublisher(KafkaTemplate<String, UserSignedInEvent> template) {
        this.setKafkaTemplate(template);
    }
}
