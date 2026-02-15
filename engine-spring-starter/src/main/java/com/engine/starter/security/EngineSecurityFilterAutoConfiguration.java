package com.engine.starter.security;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.engine.starter.EngineSecurityProperties;

/**
 * Configures Spring Security when only the starter is on the classpath so that
 * public routes (/, /home, /auth/**) work without redirect to /login.
 * Does not register if the app (or engine-spring-adapter) already defines a
 * SecurityFilterChain.
 */
@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(SecurityFilterChain.class)
@EnableWebSecurity
@EnableConfigurationProperties(EngineSecurityProperties.class)
public class EngineSecurityFilterAutoConfiguration {

    private static final List<String> DEFAULT_PUBLIC_PATHS = List.of(
            "/",
            "/home",
            "/auth/**",
            "/error",
            "/actuator/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
    );

    /** Chain that matches only public paths and permits all (no auth). Runs first. */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain enginePublicSecurityFilterChain(HttpSecurity http,
            EngineSecurityProperties securityProperties) throws Exception {

        List<String> paths = securityProperties.getPublicPaths();
        if (paths == null || paths.isEmpty()) {
            paths = DEFAULT_PUBLIC_PATHS;
        }
        String[] publicPaths = paths.toArray(String[]::new);

        http
            .securityMatcher(publicPaths)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    /** Chain for all other requests: stateless, 401 entry point, JWT-style auth. */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public SecurityFilterChain engineSecurityFilterChain(HttpSecurity http,
            EngineSecurityProperties securityProperties) throws Exception {

        List<String> paths = securityProperties.getPublicPaths();
        if (paths == null || paths.isEmpty()) {
            paths = DEFAULT_PUBLIC_PATHS;
        }
        String[] publicPaths = paths.toArray(String[]::new);

        http
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .logout(logout -> logout.disable())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(new JsonUnauthorizedEntryPoint()))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(publicPaths).permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            );

        return http.build();
    }
}
