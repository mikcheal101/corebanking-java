package com.mikky.corebanking.audit.infrastructure.persistence.command;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.mikky.corebanking.audit.domain.model.AuditLog;

@Repository
public interface AuditLogCommandRepository extends MongoRepository<AuditLog, String> {

}
