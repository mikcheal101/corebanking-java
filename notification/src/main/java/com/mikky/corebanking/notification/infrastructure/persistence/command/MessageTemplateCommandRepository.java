package com.mikky.corebanking.notification.infrastructure.persistence.command;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikky.corebanking.notification.domain.model.MessageTemplate;

@Repository
public interface MessageTemplateCommandRepository extends JpaRepository<MessageTemplate, UUID> {
}
