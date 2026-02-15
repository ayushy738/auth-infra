package com.engine.starter.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Returns 401 JSON for unauthenticated requests instead of redirecting to /login.
 */
public class JsonUnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private static final String BODY =
            "{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(BODY);
    }
}
