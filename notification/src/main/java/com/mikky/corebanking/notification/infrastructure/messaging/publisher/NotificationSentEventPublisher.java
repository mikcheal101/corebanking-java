package com.mikky.corebanking.notification.infrastructure.messaging.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import com.mikky.corebanking.events.domain.event.notification.NotificationSentEvent;
import com.mikky.corebanking.events.infrastructure.messaging.publisher.KafkaEventPublisher;

public class NotificationSentEventPublisher extends KafkaEventPublisher<NotificationSentEvent> {

    public NotificationSentEventPublisher(KafkaTemplate<String, NotificationSentEvent> template) {
        this.setKafkaTemplate(template);
    }
}
