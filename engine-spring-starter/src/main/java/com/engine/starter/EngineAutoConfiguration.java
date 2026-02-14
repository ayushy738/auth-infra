package com.engine.starter;

import com.engine.core.services.AuthService;
import com.engine.core.ports.*;
import com.engine.starter.defaults.*;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(EngineSecurityProperties.class)
@ConditionalOnClass(AuthService.class)
public class EngineAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UserRepositoryPort userRepositoryPort() {
        return new InMemoryUserRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public RefreshTokenRepositoryPort refreshTokenRepositoryPort() {
        return new InMemoryRefreshTokenRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenBlacklistPort tokenBlacklistPort() {
        return new InMemoryTokenBlacklist();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditLogPort auditLogPort() {
        return new SimpleAuditLogger();
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoderPort passwordEncoderPort() {
        return new DefaultPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthService authService(
            UserRepositoryPort userRepository,
            RefreshTokenRepositoryPort refreshRepo,
            PasswordEncoderPort passwordEncoder,
            TokenServicePort tokenService,
            TokenBlacklistPort blacklistPort,
            AuditLogPort auditLogPort
    ) {
        return new AuthService(
                userRepository,
                refreshRepo,
                passwordEncoder,
                tokenService,
                blacklistPort,
                auditLogPort
        );
    }
}