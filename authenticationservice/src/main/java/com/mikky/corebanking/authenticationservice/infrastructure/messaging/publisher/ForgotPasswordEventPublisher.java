package com.mikky.corebanking.authenticationservice.infrastructure.messaging.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.authenticationservice.domain.event.ForgotPasswordEvent;

@Component
public class ForgotPasswordEventPublisher extends KafkaEventPublisher<ForgotPasswordEvent> {

    public ForgotPasswordEventPublisher(KafkaTemplate<String, ForgotPasswordEvent> kafkaTemplate) {
        this.setKafkaTemplate(kafkaTemplate);
    }
}
