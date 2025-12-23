package com.mikky.corebanking.audit.application.query.dto;

import lombok.Data;

@Data
public class AuditLogRequest {

    private int pageSize = 0; // default page
    private int pageNumber = 100; // default page size
}
