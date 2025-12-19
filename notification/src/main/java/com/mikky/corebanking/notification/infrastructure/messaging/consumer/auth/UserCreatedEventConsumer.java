package com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.UserCreatedEvent;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;

@Component
public class UserCreatedEventConsumer implements DomainEventConsumer<UserCreatedEvent> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void consume(UserCreatedEvent event) {
        log.info("Processing UserCreatedEvent: {}", event);
    }

    @Override
    public String getTopic() {
        return EventType.USER_CREATED.getTopic();
    }

    @Override
    public Class<UserCreatedEvent> getEventClass() {
        return UserCreatedEvent.class;
    }
}
