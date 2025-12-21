package com.mikky.corebanking.notification.infrastructure.messaging.consumer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikky.corebanking.events.domain.event.Event;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaEventConsumer {

    protected Logger log = LoggerFactory.getLogger(getClass());
    private final ConsumerFactory<String, String> consumerFactory;
    private final List<DomainEventConsumer<?>> consumers;
    private final ObjectMapper objectMapper;
    private ConcurrentHashMap<String, ConcurrentMessageListenerContainer<String, String>> containers = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        for(DomainEventConsumer<?> consumer: consumers) {
            this.startConsumer(consumer);
        }
    }

    private <T extends Event> void startConsumer(DomainEventConsumer<T> consumer) {
        String topic = consumer.getTopic();
        var containerProperties = new ContainerProperties(topic);
        containerProperties.setMessageListener((MessageListener<String, T>) messageListener -> {
            try {
                // T event = objectMapper.readValue(messageListener.value(), consumer.getEventClass());
                consumer.consume(messageListener.value());

                log.info("Successfully processed event from topic {}: {}", topic, messageListener.value());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        containerProperties.setAckMode(ContainerProperties.AckMode.RECORD);

        var container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
        container.setConcurrency(1);
        container.start();
        containers.put(topic, container);
        log.info("Kafka started consuming for {}", topic);
    }

    private void stopConsumer(String topic) {
        var container = containers.get(topic);
        if (container != null) {
            container.stop();
            containers.remove(topic);
            log.info("Stopped kafka consumer for topic: {}", topic);
        }
    }
}
