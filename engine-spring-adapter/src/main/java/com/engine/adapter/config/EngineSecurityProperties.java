package com.engine.adapter.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "engine.security")
public class EngineSecurityProperties {

    private String jwtSecret;
    private long accessExpiration;
    private long refreshExpiration;

    /**
     * Ant-style path patterns that do not require authentication.
     * Defaults allow home, auth endpoints, error and actuator.
     */
    private List<String> publicPaths = List.of(
            "/",
            "/home",
            "/auth/**",
            "/error",
            "/actuator/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
    );

    public String getJwtSecret() { return jwtSecret; }
    public void setJwtSecret(String jwtSecret) { this.jwtSecret = jwtSecret; }

    public long getAccessExpiration() { return accessExpiration; }
    public void setAccessExpiration(long accessExpiration) {
        this.accessExpiration = accessExpiration;
    }

    public long getRefreshExpiration() { return refreshExpiration; }
    public void setRefreshExpiration(long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }

    public List<String> getPublicPaths() { return publicPaths; }
    public void setPublicPaths(List<String> publicPaths) { this.publicPaths = publicPaths; }
}