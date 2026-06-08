package com.engine.starter.security;

import java.security.Key;
import java.util.Date;
import java.util.UUID;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.engine.starter.config.EngineSecurityProperties;
import com.engine.core.domain.User;
import com.engine.core.ports.TokenServicePort;

public class JwtServiceAdapter implements TokenServicePort {

    private final EngineSecurityProperties props;
    public JwtServiceAdapter(EngineSecurityProperties props) {
        this.props = props;
    }
    private final String SECRET = "very-secret-key-should-be-env";
    private final long ACCESS_EXPIRATION = 1000 * 60 * 15; // 15 min

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(props.getJwtSecret().getBytes());
    }


    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + props.getAccessExpiration())
                )
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extractUserEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}