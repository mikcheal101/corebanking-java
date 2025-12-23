package com.mikky.corebanking.audit.infrastructure.persistence.query;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mikky.corebanking.audit.domain.model.AuditLog;

@Repository
public interface AuditLogQueryRepository extends MongoRepository<AuditLog, String> {
}
