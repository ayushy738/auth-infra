package com.engine.starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.engine.core.ports.AuditLogPort;
import com.engine.core.ports.PasswordEncoderPort;
import com.engine.core.ports.RefreshTokenRepositoryPort;
import com.engine.core.ports.TokenBlacklistPort;
import com.engine.core.ports.UserRepositoryPort;
import com.engine.starter.persistence.AuditLogAdapter;
import com.engine.starter.persistence.JpaRefreshTokenRepository;
import com.engine.starter.persistence.JpaUserRepository;
import com.engine.starter.persistence.RefreshTokenRepositoryAdapter;
import com.engine.starter.persistence.TokenBlacklistAdapter;
import com.engine.starter.persistence.UserRepositoryAdapter;
import com.engine.starter.security.BCryptPasswordAdapter;
import com.engine.starter.security.JwtAuthenticationFilter;
import com.engine.starter.security.JwtServiceAdapter;
import com.engine.starter.security.UserDetailsServiceAdapter;

/**
 * Explicit @Bean factory for all security and persistence components.
 *
 * In a Spring Boot starter library, classes annotated with @Component / @Service /
 * @Repository are NEVER picked up automatically because the consuming application's
 * @SpringBootApplication does NOT component-scan com.engine.starter.*.
 * Auto-configurations must declare every bean they need as an explicit @Bean method.
 */
@Configuration
public class EngineSecurityBeansConfiguration {

    // ── Persistence adapters ──────────────────────────────────────────────────

    @Bean
    @ConditionalOnMissingBean(UserRepositoryPort.class)
    public UserRepositoryPort userRepositoryPort(JpaUserRepository jpaRepo) {
        return new UserRepositoryAdapter(jpaRepo);
    }

    @Bean
    @ConditionalOnMissingBean(RefreshTokenRepositoryPort.class)
    public RefreshTokenRepositoryPort refreshTokenRepositoryPort(
            JpaRefreshTokenRepository jpaRepo) {
        return new RefreshTokenRepositoryAdapter(jpaRepo);
    }

    @Bean
    @ConditionalOnMissingBean(TokenBlacklistPort.class)
    public TokenBlacklistPort tokenBlacklistPort() {
        return new TokenBlacklistAdapter();
    }

    @Bean
    @ConditionalOnMissingBean(AuditLogPort.class)
    public AuditLogPort auditLogPort() {
        return new AuditLogAdapter();
    }

    // ── Security beans ────────────────────────────────────────────────────────

    @Bean
    @ConditionalOnMissingBean(PasswordEncoderPort.class)
    public PasswordEncoderPort passwordEncoderPort() {
        return new BCryptPasswordAdapter();
    }

    @Bean
    @ConditionalOnMissingBean(JwtServiceAdapter.class)
    public JwtServiceAdapter jwtServiceAdapter(EngineSecurityProperties props) {
        return new JwtServiceAdapter(props);
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsServiceAdapter userDetailsServiceAdapter(JpaUserRepository jpaRepo) {
        return new UserDetailsServiceAdapter(jpaRepo);
    }

    @Bean
    @ConditionalOnMissingBean(JwtAuthenticationFilter.class)
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtServiceAdapter jwtServiceAdapter,
            UserDetailsService userDetailsService,
            TokenBlacklistPort tokenBlacklistPort) {
        return new JwtAuthenticationFilter(jwtServiceAdapter, userDetailsService, tokenBlacklistPort);
    }
}
