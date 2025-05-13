package com.unilak.expensetracker.expense_tracker.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication response with JWT token
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
}
