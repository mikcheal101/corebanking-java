package com.mikky.corebanking.audit.infrastructure.messaging.consumer.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.audit.infrastructure.persistence.command.AuditLogCommandService;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordEvent;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ForgotPasswordEventConsumer implements DomainEventConsumer<ForgotPasswordEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final AuditLogCommandService auditLogCommandService;

    @Override
    public Class<ForgotPasswordEvent> getEventClass() {
        return ForgotPasswordEvent.class;
    }

    @Override
    public String getTopic() {
        return EventType.FORGOT_PASSWORD.getTopic();
    }

    @Override
    public void consume(ForgotPasswordEvent event) {
        try {
            this.auditLogCommandService.createAuditLog(event);
            this.logger.info("Consumed {}", this.getTopic());
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
    }
}
