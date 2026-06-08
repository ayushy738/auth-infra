package com.engine.starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Auto-configuration that wires the full engine security stack:
 *  - EngineSecurityBeansConfiguration: all @Bean declarations (JWT filter, adapters, etc.)
 *  - SecurityConfig: the SecurityFilterChain with JWT filter applied
 *
 * JPA repos and entities in com.engine.starter.persistence are explicitly enabled
 * here so they are available even when the consuming app's @SpringBootApplication
 * does not scan com.engine.starter.
 */
@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(SecurityFilterChain.class)
@EnableJpaRepositories(basePackages = "com.engine.starter.persistence")
@EntityScan(basePackages = "com.engine.starter.persistence")
@Import({ EngineSecurityBeansConfiguration.class, SecurityConfig.class })
public class EngineWebSecurityAutoConfiguration {
}
