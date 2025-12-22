package com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordEvent;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.strategy.NotificationStrategyResolver;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ForgotPasswordEventConsumer implements DomainEventConsumer<ForgotPasswordEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final NotificationStrategyResolver notificationStrategyResolver;

    @Override
    public String getTopic() {
        return EventType.FORGOT_PASSWORD.getTopic();
    }

    @Override
    public Class<ForgotPasswordEvent> getEventClass() {
        return ForgotPasswordEvent.class;
    }

    @Override
    public void consume(ForgotPasswordEvent event) {
        event.getPayload().getChannels()
                .forEach(channel -> this.notificationStrategyResolver.resolveAndSend(event, channel));
        logger.info("Processing event: {}", event);
    }
}
