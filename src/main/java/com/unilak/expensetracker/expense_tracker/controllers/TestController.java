package com.unilak.expensetracker.expense_tracker.controllers;

import com.unilak.expensetracker.expense_tracker.payloads.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for testing security
 * @author YourName
 * @reg YourRegistrationNumber
 */
@RestController
@RequestMapping("/api/v1/test")
@Tag(name = "Test", description = "Security test endpoints")
public class TestController {

    /**
     * Public endpoint that doesn't require authentication
     * @return Welcome message
     */
    @GetMapping("/public")
    @Operation(summary = "Public endpoint", description = "This endpoint is accessible without authentication")
    public ResponseEntity<ApiResponse<Map<String, String>>> publicEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a public endpoint - no authentication required");
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Protected endpoint that requires authentication
     * @return User info
     */
    @GetMapping("/protected")
    @Operation(
            summary = "Protected endpoint",
            description = "This endpoint requires authentication",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<Map<String, String>>> protectedEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a protected endpoint - authentication required");
        response.put("user", auth.getName());
        response.put("authorities", auth.getAuthorities().toString());

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}