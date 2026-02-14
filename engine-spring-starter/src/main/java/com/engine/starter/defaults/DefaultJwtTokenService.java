package com.engine.starter.defaults;

import com.engine.core.domain.User;
import com.engine.core.ports.TokenServicePort;
import com.engine.starter.EngineSecurityProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class DefaultJwtTokenService implements TokenServicePort {

    private final EngineSecurityProperties props;

    public DefaultJwtTokenService(EngineSecurityProperties props) {
        this.props = props;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(props.getJwtSecret().getBytes());
    }

    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                        System.currentTimeMillis() + props.getAccessExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                        System.currentTimeMillis() + props.getRefreshExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
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
}