package com.mikky.corebanking.notification.infrastructure.persistence.query;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.notification.Channel;
import com.mikky.corebanking.notification.domain.model.MessageTemplate;

@Repository
public interface MessageTemplateQueryRepository extends JpaRepository<MessageTemplate, UUID> {
    Optional<MessageTemplate> findByChannelAndEventType(Channel channel, EventType eventType);
}
