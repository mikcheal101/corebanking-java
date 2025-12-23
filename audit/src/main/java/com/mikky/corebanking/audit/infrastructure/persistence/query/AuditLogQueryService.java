package com.mikky.corebanking.audit.infrastructure.persistence.query;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.mikky.corebanking.audit.application.query.dto.AuditLogRequest;
import com.mikky.corebanking.audit.application.query.dto.AuditLogResponse;
import com.mikky.corebanking.audit.domain.model.AuditLog;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogQueryService {

    private final AuditLogQueryRepository auditLogQueryRepository;

    public List<AuditLogResponse> fetchLogs(AuditLogRequest auditLogRequest) {
        Pageable pageable = PageRequest.of(auditLogRequest.getPageNumber(), auditLogRequest.getPageSize());

        return this.auditLogQueryRepository.findAll(pageable)
                .stream()
                .map(this::convertToAuditLogResponse)
                .toList();
    }

    private AuditLogResponse convertToAuditLogResponse(AuditLog auditLog) {
        return AuditLogResponse
                .builder()
                .id(auditLog.getId())
                .eventType(auditLog.getEventType().toString())
                .occurredAt(auditLog.getOccurredAt())
                .service(auditLog.getService())
                .payload(auditLog.getPayload())
                .payloadHash(auditLog.getPayloadHash())
                .build();
    }
}
