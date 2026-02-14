package com.engine.starter;

import com.engine.core.services.AuthService;
import com.engine.core.ports.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(EngineSecurityProperties.class)
@ConditionalOnClass(AuthService.class)
public class EngineAutoConfiguration {

    // Create AuthService only if missing
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