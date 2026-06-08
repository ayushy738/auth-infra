package com.engine.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.engine.core.services.AuthService;
import com.engine.starter.controllers.AuthController;

@Configuration
public class AuthControllerConfiguration {

    @Bean
    public AuthController authController(AuthService authService) {
        System.out.println("AUTH CONTROLLER REGISTERED");
        return new AuthController(authService);
    }
}