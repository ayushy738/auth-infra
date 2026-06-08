package com.engine.starter.config;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.engine.starter.security.JwtAuthenticationFilter;
import com.engine.starter.security.JsonUnauthorizedEntryPoint;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(EngineSecurityProperties.class)
public class SecurityConfig {

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

    private final JwtAuthenticationFilter jwtFilter;
    private final EngineSecurityProperties securityProperties;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter,
            EngineSecurityProperties securityProperties) {
        this.jwtFilter = jwtFilter;
        this.securityProperties = securityProperties;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

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
            )
            .addFilterBefore(jwtFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}