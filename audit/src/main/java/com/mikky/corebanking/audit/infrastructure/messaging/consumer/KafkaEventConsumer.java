package com.mikky.corebanking.audit.infrastructure.messaging.consumer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.domain.event.Event;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final ConsumerFactory<String, String> consumerFactory;
    private final List<DomainEventConsumer<?>> consumers;
    private final ConcurrentHashMap<String, ConcurrentMessageListenerContainer<String, String>> containers;

    @PostConstruct
    public void init() {
        for (DomainEventConsumer<?> consumer : this.consumers) {
            this.startConsumer(consumer);
        }
    }

    private <T extends Event> void startConsumer(DomainEventConsumer<T> consumer) {
        var containerProperties = new ContainerProperties(consumer.getTopic());
        containerProperties.setMessageListener((MessageListener<String, T>) messageListener -> {
            try {
                consumer.consume(messageListener.value());
                this.logger.info("Successfully processed event from topic {}: {}", consumer.getTopic(),
                        messageListener.value());
            } catch (Exception e) {
                this.logger.error(e.getMessage());
            }
        });
        containerProperties.setAckMode(ContainerProperties.AckMode.RECORD);

        var concurrentMessageListenerContainer = new ConcurrentMessageListenerContainer<>(this.consumerFactory, containerProperties);
        concurrentMessageListenerContainer.setConcurrency(1);
        concurrentMessageListenerContainer.start();
        
        this.containers.put(consumer.getTopic(), concurrentMessageListenerContainer);
        this.logger.info("Kafka Started consuming for {}", consumer.getTopic());
    }

}
