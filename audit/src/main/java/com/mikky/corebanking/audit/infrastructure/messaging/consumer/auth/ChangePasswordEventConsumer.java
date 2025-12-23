package com.mikky.corebanking.audit.infrastructure.messaging.consumer.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mikky.corebanking.audit.infrastructure.persistence.command.AuditLogCommandService;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordChangeEvent;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChangePasswordEventConsumer implements DomainEventConsumer<ForgotPasswordChangeEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final AuditLogCommandService auditLogCommandService;

    @Override
    public Class<ForgotPasswordChangeEvent> getEventClass() {
        return ForgotPasswordChangeEvent.class;
    }

    @Override
    public String getTopic() {
        return EventType.FORGOT_PASSWORD_CHANGED.getTopic();
    }

    @Override
    public void consume(ForgotPasswordChangeEvent event) {
        try {
            this.auditLogCommandService.createAuditLog(event);
            this.logger.info("Consumed {}", this.getTopic());
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
    }
}
