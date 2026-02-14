package com.engine.core.domain;

public class BlacklistedToken {

    private final String token;
    private final long expiry;

    public BlacklistedToken(String token, long expiry) {
        this.token = token;
        this.expiry = expiry;
    }

    public String getToken() { return token; }
    public long getExpiry() { return expiry; }
}