package com.engine.core.domain;

import java.time.Instant;

public class RefreshToken {

    private final String token;
    private final String userId;
    private final Instant expiry;
    private final boolean revoked;

    public RefreshToken(String token, String userId, Instant expiry, boolean revoked) {
        this.token = token;
        this.userId = userId;
        this.expiry = expiry;
        this.revoked = revoked;
    }

    public String getToken() { return token; }
    public String getUserId() { return userId; }
    public Instant getExpiry() { return expiry; }
    public boolean isRevoked() { return revoked; }

    public boolean isExpired() {
        return Instant.now().isAfter(expiry);
    }
}
