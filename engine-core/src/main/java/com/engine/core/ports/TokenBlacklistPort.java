package com.engine.core.ports;

import java.util.Optional;

public interface TokenBlacklistPort {

    void blacklist(String token, long expiry);

    boolean isBlacklisted(String token);
}