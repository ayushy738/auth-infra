package com.engine.core.ports;

import com.engine.core.domain.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepositoryPort {

    void save(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);

    void revoke(String token);
}