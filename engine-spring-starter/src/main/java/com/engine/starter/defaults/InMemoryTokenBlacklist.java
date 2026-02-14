package com.engine.starter.defaults;

import com.engine.core.ports.TokenBlacklistPort;

import java.util.HashSet;
import java.util.Set;

public class InMemoryTokenBlacklist implements TokenBlacklistPort {

    private final Set<String> blacklist = new HashSet<>();

    @Override
    public void blacklist(String token, long expiry) {
        blacklist.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}