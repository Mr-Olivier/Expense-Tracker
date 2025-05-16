package com.unilak.expensetracker.expense_tracker.payloads.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for category request
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Data
public class CategoryRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Type is required")
    private String type; // income or expense
}