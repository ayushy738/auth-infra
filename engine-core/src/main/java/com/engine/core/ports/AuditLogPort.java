package com.engine.core.ports;

import com.engine.core.domain.AuditLog;

public interface AuditLogPort {
    void save(AuditLog log);
}