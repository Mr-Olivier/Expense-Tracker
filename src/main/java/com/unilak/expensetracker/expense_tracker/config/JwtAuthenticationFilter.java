
package com.unilak.expensetracker.expense_tracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unilak.expensetracker.expense_tracker.entities.User;
import com.unilak.expensetracker.expense_tracker.payloads.response.ApiResponse;
import com.unilak.expensetracker.expense_tracker.repositories.UserRepository;
import com.unilak.expensetracker.expense_tracker.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    // Constructor injection instead of field injection
    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserRepository userRepository, ObjectMapper objectMapper) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Skip authentication for public endpoints
        String requestPath = request.getRequestURI();
        if (requestPath.contains("/api/v1/auth/") ||
                requestPath.contains("/swagger-ui") ||
                requestPath.contains("/api-docs") ||
                requestPath.contains("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                sendUnauthorizedError(response, "Authorization header is missing or invalid");
                return;
            }

            String token = authHeader.substring(7);

            if (!jwtUtils.validateToken(token)) {
                sendUnauthorizedError(response, "Invalid or expired token");
                return;
            }

            String email = jwtUtils.extractEmail(token);
            String role = jwtUtils.extractRole(token);

            // Verify user exists and is active
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if ("banned".equals(user.getStatus())) {
                sendForbiddenError(response, "Your account has been banned. Please contact admin.");
                return;
            }

            // Set authentication in Security Context
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(role)
            );

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email, null, authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            sendUnauthorizedError(response, "Authentication failed: " + e.getMessage());
        }
    }

    private void sendUnauthorizedError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<String> errorResponse = ApiResponse.error(message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private void sendForbiddenError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<String> errorResponse = ApiResponse.error(message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}