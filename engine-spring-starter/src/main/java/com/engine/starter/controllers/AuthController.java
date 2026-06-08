package com.engine.starter.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.engine.core.domain.AuthResponse;
import com.engine.core.services.AuthService;
import com.engine.starter.dto.RefreshRequest;
import com.engine.starter.dto.RegisterRequest;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(
                authService.register(req.getEmail(), req.getPassword())
        );
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("auth service up");
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
        @RequestBody RefreshRequest request) {

        return ResponseEntity.ok(
                authService.refresh(request.getRefreshToken())
        );
    }
}