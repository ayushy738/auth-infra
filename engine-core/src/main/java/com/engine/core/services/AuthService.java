package com.engine.core.services;

import com.engine.core.domain.*;
import com.engine.core.ports.*;
import com.engine.core.exceptions.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class AuthService {

    private final UserRepositoryPort userRepository;
    private final RefreshTokenRepositoryPort refreshRepo;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenServicePort tokenService;
    private final TokenBlacklistPort tokenBlacklistPort;
    private final AuditLogPort auditLogPort;

    public AuthService(UserRepositoryPort userRepository,
                       RefreshTokenRepositoryPort refreshRepo,
                       PasswordEncoderPort passwordEncoder,
                       TokenServicePort tokenService,
                       TokenBlacklistPort tokenBlacklistPort,
                       AuditLogPort auditLogPort) {
        this.userRepository = userRepository;
        this.refreshRepo = refreshRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.tokenBlacklistPort = tokenBlacklistPort;
        this.auditLogPort = auditLogPort;
    }

    public AuthResponse register(String email, String password) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        String hashed = passwordEncoder.encode(password);

        User user = new User(
                UUID.randomUUID().toString(),
                email,
                hashed,
                Role.USER
        );

        userRepository.save(user);

        return generateTokens(user);
    }

    public AuthResponse login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return generateTokens(user);
    }

    public AuthResponse refresh(String refreshToken) {

        RefreshToken storedToken = refreshRepo.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (storedToken.isExpired() || storedToken.isRevoked()) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        User user = userRepository.findByEmail(
                tokenService.extractUserEmail(refreshToken)
        ).orElseThrow(() -> new RuntimeException("User not found"));    

        // Rotation: revoke old token
        refreshRepo.revoke(refreshToken);

        return generateTokens(user);
    }

    private AuthResponse generateTokens(User user) {

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        RefreshToken token = new RefreshToken(
                refreshToken,
                user.getId(),
                Instant.now().plus(7, ChronoUnit.DAYS),
                false
        );

        refreshRepo.save(token);

        return new AuthResponse(accessToken, refreshToken);
    }
    public void logout(String accessToken, String email) {
        long expiry = System.currentTimeMillis() + 900000;
        tokenBlacklistPort.blacklist(accessToken, expiry);
        auditLogPort.save(
            new AuditLog(email, "LOGOUT_SUCCESS", System.currentTimeMillis())
        );
    }
}