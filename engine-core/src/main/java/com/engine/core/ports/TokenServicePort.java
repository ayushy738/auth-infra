package com.engine.core.ports;

import com.engine.core.domain.User;

public interface TokenServicePort {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    boolean validateAccessToken(String token);

    String extractUserEmail(String token);
}