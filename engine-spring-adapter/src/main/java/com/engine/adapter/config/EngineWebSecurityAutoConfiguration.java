package com.engine.adapter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Registers the engine security config when the adapter is on the classpath,
 * so that public routes (/, /home, /auth/**) work without redirect to /login
 * even when the consuming app does not scan com.engine.adapter.
 */
@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(SecurityFilterChain.class)
@Import(SecurityConfig.class)
public class EngineWebSecurityAutoConfiguration {
}
