package com.engine.starter.defaults;

import com.engine.core.domain.AuditLog;
import com.engine.core.ports.AuditLogPort;

public class SimpleAuditLogger implements AuditLogPort {

    @Override
    public void save(AuditLog log) {
        System.out.println("AUDIT: " + log);
    }
}