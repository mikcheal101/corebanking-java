package com.mikky.corebanking.audit.infrastructure.messaging.consumer;

import java.util.List;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.KafkaEventConsumer;

@Component
public class KafkaInitEventConsumer extends KafkaEventConsumer {
    public KafkaInitEventConsumer(
            ConsumerFactory<String, String> consumerFactory,
            List<DomainEventConsumer<?>> consumers) {
        super(consumerFactory, consumers);
    }
}