package com.engine.core.domain;

public class AuditLog {

    private final String userEmail;
    private final String action;
    private final long timestamp;

    public AuditLog(String userEmail, String action, long timestamp) {
        this.userEmail = userEmail;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getAction() {
        return action;
    }

    public long getTimestamp() {
        return timestamp;
    }
}