package com.mikky.corebanking.audit.application.query.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {
    private String id;
    private Instant occurredAt;
    private String service;
    private String eventType;
    private String payloadHash;
    private String payload;
}
