package com.engine.starter.persistence;

import com.engine.core.domain.AuditLog;
import com.engine.core.ports.AuditLogPort;

public class AuditLogAdapter implements AuditLogPort {

    @Override
    public void save(AuditLog log) {
        // Simple implementation - can be extended to use JPA repository if needed
        System.out.println("Audit Log: " + log.getUserEmail() + " - " + log.getAction() + " at " + log.getTimestamp());
    }
}

