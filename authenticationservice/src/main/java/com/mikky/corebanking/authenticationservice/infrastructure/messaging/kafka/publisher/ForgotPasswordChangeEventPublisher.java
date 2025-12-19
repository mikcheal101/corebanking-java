package com.mikky.corebanking.authenticationservice.infrastructure.messaging.kafka.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordChangeEvent;
import com.mikky.corebanking.events.infrastructure.messaging.publisher.KafkaEventPublisher;

@Component
public class ForgotPasswordChangeEventPublisher extends KafkaEventPublisher<ForgotPasswordChangeEvent> {

    public ForgotPasswordChangeEventPublisher(KafkaTemplate<String, ForgotPasswordChangeEvent> template) {
        this.setKafkaTemplate(template);
    }
}
