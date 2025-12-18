package com.mikky.corebanking.authenticationservice.infrastructure.messaging.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.auth.ForgotPasswordChangeEvent;

@Component
public class ForgotPasswordChangeEventPublisher extends KafkaEventPublisher<ForgotPasswordChangeEvent> {

    public ForgotPasswordChangeEventPublisher(KafkaTemplate<String, ForgotPasswordChangeEvent> template) {
        this.setKafkaTemplate(template);
    }
}
