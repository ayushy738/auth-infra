package com.engine.starter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.engine.core.ports.UserRepositoryPort;
import com.engine.core.ports.PasswordEncoderPort;
import com.engine.core.ports.TokenServicePort;
import com.engine.core.ports.RefreshTokenRepositoryPort;
import com.engine.core.ports.TokenBlacklistPort;
import com.engine.core.ports.AuditLogPort;
import com.engine.core.services.AuthService;

@Configuration
public class AuthServiceConfig {

    @Bean
    public AuthService authService(UserRepositoryPort repo,
                                   RefreshTokenRepositoryPort refreshRepo,
                                   PasswordEncoderPort encoder,
                                   TokenServicePort tokenService,
                                   TokenBlacklistPort tokenBlacklistPort,
                                   AuditLogPort auditLogPort) {
        return new AuthService(repo, refreshRepo, encoder, tokenService, tokenBlacklistPort, auditLogPort);
    }
}