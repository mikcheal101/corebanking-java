package com.mikky.corebanking.notification.infrastructure.messaging.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventConsumer {

    private Logger log = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = "auth.user-created")
    public void consume(String message) {
        log.info("Consumer consumed the message: {}", message);
    }
}
