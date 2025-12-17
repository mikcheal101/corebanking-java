package com.mikky.corebanking.authenticationservice.infrastructure.messaging.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import com.mikky.corebanking.authenticationservice.domain.event.Event;
import com.mikky.corebanking.authenticationservice.domain.exceptions.KafkaUnInitializedException;
import lombok.Setter;


@Setter
public abstract class KafkaEventPublisher<T extends Event> implements DomainEventPublisher<T> {
    
    private KafkaTemplate<String, T> kafkaTemplate;
    protected final Logger logger;

    protected KafkaEventPublisher() {
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void publish(T event) throws KafkaUnInitializedException {
        if (this.kafkaTemplate == null) {
            throw new KafkaUnInitializedException();
        }

        var topic = event.getEventType().getTopic();

        this.logger.info("Publishing event to topic [{}]: {}", topic , event);
        this.kafkaTemplate.send(topic, event);
    }
}
