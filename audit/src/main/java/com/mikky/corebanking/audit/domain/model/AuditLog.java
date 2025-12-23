package com.mikky.corebanking.audit.domain.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.mikky.corebanking.events.domain.event.EventType;

@Data
@Document(collection = "audit_logs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    private String id;
    private Instant occurredAt;
    private String service;
    private EventType eventType;
    private String payloadHash;
    private String payload;
}
