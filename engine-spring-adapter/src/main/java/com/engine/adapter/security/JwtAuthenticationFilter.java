package com.engine.adapter.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.engine.core.ports.TokenBlacklistPort;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceAdapter jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistPort blacklistPort;

    public JwtAuthenticationFilter(JwtServiceAdapter jwtService,
            UserDetailsService userDetailsService, TokenBlacklistPort blacklistPort) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.blacklistPort = blacklistPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String token = header.substring(7);
        if (blacklistPort.isBlacklisted(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        String email = jwtService.extractUserEmail(token);
        
        if (email != null && jwtService.validateAccessToken(token)) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

            SecurityContextHolder.getContext()
                    .setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
