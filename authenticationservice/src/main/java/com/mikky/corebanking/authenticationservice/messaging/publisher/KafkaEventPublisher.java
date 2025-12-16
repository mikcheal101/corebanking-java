package com.mikky.corebanking.authenticationservice.messaging.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.authenticationservice.domain.event.Event;
import com.mikky.corebanking.authenticationservice.domain.exceptions.KafkaUnInitializedException;
import lombok.Setter;

@Component
@Setter
public abstract class KafkaEventPublisher extends EventPublisher {
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(Event event) throws KafkaUnInitializedException {
        if (this.kafkaTemplate == null) {
            throw new KafkaUnInitializedException();
        }
        this.kafkaTemplate.send(event.getEventType().getTopic(), event);
    }
}
