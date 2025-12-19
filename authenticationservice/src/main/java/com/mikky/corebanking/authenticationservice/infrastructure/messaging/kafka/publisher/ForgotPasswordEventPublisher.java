package com.mikky.corebanking.authenticationservice.infrastructure.messaging.kafka.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordEvent;
import com.mikky.corebanking.events.infrastructure.messaging.publisher.KafkaEventPublisher;

@Component
public class ForgotPasswordEventPublisher extends KafkaEventPublisher<ForgotPasswordEvent> {

    public ForgotPasswordEventPublisher(KafkaTemplate<String, ForgotPasswordEvent> kafkaTemplate) {
        this.setKafkaTemplate(kafkaTemplate);
    }
}
