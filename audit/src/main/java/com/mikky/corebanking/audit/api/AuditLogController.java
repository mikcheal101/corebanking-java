package com.mikky.corebanking.audit.api;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mikky.corebanking.audit.application.query.dto.AuditLogRequest;
import com.mikky.corebanking.audit.application.query.dto.AuditLogResponse;
import com.mikky.corebanking.audit.infrastructure.persistence.query.AuditLogQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/audit")
public class AuditLogController {

    private final AuditLogQueryService auditLogQueryService;

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> logs(AuditLogRequest request) {
        try {
            List<AuditLogResponse> response = this.auditLogQueryService.fetchLogs(request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }
}
