package com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.UserSignedInEvent;

public class UserSignedInEventConsumer implements DomainEventConsumer<UserSignedInEvent> {

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Class<UserSignedInEvent> getEventClass() {
        return UserSignedInEvent.class;
    }

    @Override
    public String getTopic() {
        return EventType.USER_LOGGED_IN.getTopic();
    }

    @Override
    public void consume(UserSignedInEvent event) {
        log.info("Processing event: {}", event);
    }
}
