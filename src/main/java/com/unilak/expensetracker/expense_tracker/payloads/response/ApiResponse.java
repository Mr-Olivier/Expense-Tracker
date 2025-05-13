package com.unilak.expensetracker.expense_tracker.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response as per project requirements
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private T data;

    // Helper method to create success response
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", data);
    }

    // Helper method to create error response
    public static <T> ApiResponse<T> error(T error) {
        return new ApiResponse<>("error", error);
    }
}