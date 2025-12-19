package com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordChangeEvent;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;

public class ChangePasswordEventConsumer implements DomainEventConsumer<ForgotPasswordChangeEvent> {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getTopic() {
        return EventType.FORGOT_PASSWORD_CHANGED.getTopic();
    }

    @Override
    public Class<ForgotPasswordChangeEvent> getEventClass() {
        return ForgotPasswordChangeEvent.class;
    }

    @Override
    public void consume(ForgotPasswordChangeEvent event) {
        logger.info("Processing event: {}", event);
    }
}
