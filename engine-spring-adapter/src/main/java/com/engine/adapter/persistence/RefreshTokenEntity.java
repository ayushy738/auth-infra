package com.engine.adapter.persistence;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.engine.core.domain.RefreshToken;

@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {

    @Id
    private String token;

    private String userId;
    private Instant expiry;
    private boolean revoked;

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public RefreshToken toDomain() {
        return new RefreshToken(token, userId, expiry, revoked);
    }

    public static RefreshTokenEntity fromDomain(RefreshToken token) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.token = token.getToken();
        entity.userId = token.getUserId();
        entity.expiry = token.getExpiry();
        entity.revoked = token.isRevoked();
        return entity;
    }
}