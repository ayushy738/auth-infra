package com.engine.starter.defaults;

import com.engine.core.domain.RefreshToken;
import com.engine.core.ports.RefreshTokenRepositoryPort;

import java.util.*;

public class InMemoryRefreshTokenRepository implements RefreshTokenRepositoryPort {

    private final Map<String, RefreshToken> store = new HashMap<>();

    @Override
    public void save(RefreshToken token) {
        store.put(token.getToken(), token);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return Optional.ofNullable(store.get(token));
    }

    @Override
    public void revoke(String token) {
        store.remove(token);
    }
}