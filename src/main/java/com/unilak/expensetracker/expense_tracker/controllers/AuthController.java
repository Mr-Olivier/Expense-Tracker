package com.unilak.expensetracker.expense_tracker.controllers;

import com.unilak.expensetracker.expense_tracker.payloads.request.LoginRequest;
import com.unilak.expensetracker.expense_tracker.payloads.request.SignupRequest;
import com.unilak.expensetracker.expense_tracker.payloads.response.ApiResponse;
import com.unilak.expensetracker.expense_tracker.payloads.response.JwtResponse;
import com.unilak.expensetracker.expense_tracker.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication endpoints
 * @author YourName
 * @reg YourRegistrationNumber
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "User authentication operations")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register a new user
     * @param signupRequest User signup data
     * @return JWT response with token
     */
    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token")
    public ResponseEntity<ApiResponse<JwtResponse>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        JwtResponse jwtResponse = authService.signup(signupRequest);
        return ResponseEntity.ok(ApiResponse.success(jwtResponse));
    }

    /**
     * Authenticate a user
     * @param loginRequest User login data
     * @return JWT response with token
     */
    @PostMapping("/login")
    @Operation(summary = "Authenticate a user", description = "Validates credentials and returns a JWT token")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(jwtResponse));
    }
}
