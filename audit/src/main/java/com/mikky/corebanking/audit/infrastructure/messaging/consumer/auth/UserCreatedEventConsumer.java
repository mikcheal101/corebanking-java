package com.mikky.corebanking.audit.infrastructure.messaging.consumer.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.audit.infrastructure.persistence.command.AuditLogCommandService;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.UserCreatedEvent;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserCreatedEventConsumer implements DomainEventConsumer<UserCreatedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final AuditLogCommandService auditLogCommandService;

    @Override
    public Class<UserCreatedEvent> getEventClass() {
        return UserCreatedEvent.class;
    }

    @Override
    public String getTopic() {
        return EventType.USER_CREATED.getTopic();
    }

    @Override
    public void consume(UserCreatedEvent event) {
        try {
            this.auditLogCommandService.createAuditLog(event);
            this.logger.info("Consumed {}", this.getTopic());
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
    }
}
