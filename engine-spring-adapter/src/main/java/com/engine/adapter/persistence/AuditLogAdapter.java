package com.engine.adapter.persistence;

import com.engine.core.domain.AuditLog;
import com.engine.core.ports.AuditLogPort;
import org.springframework.stereotype.Repository;

@Repository
public class AuditLogAdapter implements AuditLogPort {

    @Override
    public void save(AuditLog log) {
        // Simple implementation - can be extended to use JPA repository if needed
        System.out.println("Audit Log: " + log.getUserEmail() + " - " + log.getAction() + " at " + log.getTimestamp());
    }
}

