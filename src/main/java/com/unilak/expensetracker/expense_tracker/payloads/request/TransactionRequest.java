package com.unilak.expensetracker.expense_tracker.payloads.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO for transaction request
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Data
public class TransactionRequest {

    @NotBlank(message = "Type is required")
    private String type; // income or expense

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    private String description;
}