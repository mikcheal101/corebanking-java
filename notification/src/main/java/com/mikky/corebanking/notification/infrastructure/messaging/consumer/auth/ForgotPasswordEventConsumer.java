package com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordEvent;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;

public class ForgotPasswordEventConsumer implements DomainEventConsumer<ForgotPasswordEvent> {

    Logger logger = LoggerFactory.getLogger(getClass());

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
        logger.info("Processing event: {}", event);
    }
}
