package com.engine.starter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRefreshTokenRepository
        extends JpaRepository<RefreshTokenEntity, String> {
}