package com.engine.adapter.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.engine.core.domain.RefreshToken;
import com.engine.core.ports.RefreshTokenRepositoryPort;

@Repository
public class RefreshTokenRepositoryAdapter
        implements RefreshTokenRepositoryPort {

    private final JpaRefreshTokenRepository jpaRepo;

    public RefreshTokenRepositoryAdapter(JpaRefreshTokenRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public void save(RefreshToken token) {
        jpaRepo.save(RefreshTokenEntity.fromDomain(token));
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepo.findById(token)
                .map(RefreshTokenEntity::toDomain);
    }

    @Override
    public void revoke(String token) {
        jpaRepo.findById(token).ifPresent(entity -> {
            entity.setRevoked(true);
            jpaRepo.save(entity);
        });
    }
}