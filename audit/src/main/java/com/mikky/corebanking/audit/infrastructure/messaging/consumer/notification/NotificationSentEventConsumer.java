package com.mikky.corebanking.audit.infrastructure.messaging.consumer.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.audit.infrastructure.persistence.command.AuditLogCommandService;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.notification.NotificationSentEvent;
import com.mikky.corebanking.events.infrastructure.messaging.consumer.DomainEventConsumer;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationSentEventConsumer implements DomainEventConsumer<NotificationSentEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final AuditLogCommandService auditLogCommandService;

    @Override
    public String getTopic() {
        return EventType.NOTIFICATION_SENT.getTopic();
    }

    @Override
    public Class<NotificationSentEvent> getEventClass() {
        return NotificationSentEvent.class;
    }

    @Override
    public void consume(NotificationSentEvent notificationSentEvent) {
        try {
            this.auditLogCommandService.createAuditLog(notificationSentEvent);
            this.logger.info("Consumed {}", this.getTopic());
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
    }

}
