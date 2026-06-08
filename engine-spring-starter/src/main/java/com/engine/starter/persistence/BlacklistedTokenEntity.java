package com.engine.starter.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "blacklisted_tokens")
public class BlacklistedTokenEntity {

    @Id
    private String token;

    private long expiry;
}