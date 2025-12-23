package com.mikky.corebanking.audit.infrastructure.persistence.command;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikky.corebanking.audit.domain.model.AuditLog;
import com.mikky.corebanking.events.domain.event.Event;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogCommandService {

    private final AuditLogCommandRepository auditLogCommandRepository;
    private final ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public boolean createAuditLog(Event event) {
        try {
            String payLoadJson = this.objectMapper.writeValueAsString(event.getPayload());
            AuditLog auditLog = AuditLog.builder()
                    .eventType(event.getEventType())
                    .occurredAt(event.getOccurredAt())
                    .payload(payLoadJson)
                    .service(event.getSource())
                    .payloadHash(DigestUtils.sha512Hex(payLoadJson))
                    .build();
            this.auditLogCommandRepository.save(auditLog);
            this.logger.info("Saving auditLog details successfully!");
            return true;
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            return false;
        }
    }
}
