package com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.strategy.NotificationStrategyResolver;
import lombok.RequiredArgsConstructor;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.UserSignedInEvent;

@Component
@RequiredArgsConstructor
public class UserSignedInEventConsumer implements DomainEventConsumer<UserSignedInEvent> {

    Logger log = LoggerFactory.getLogger(getClass());
    private final NotificationStrategyResolver notificationStrategyResolver;

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
        // iterated over the payload channels and send the messages
        event.getPayload()
            .getChannels()
            .forEach(channel -> this.notificationStrategyResolver.resolveAndSend(event, channel));
        log.info("Processing event: {}", event);
    }
}
