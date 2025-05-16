package com.unilak.expensetracker.expense_tracker.payloads.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for report request
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Data
public class ReportRequest {

    @NotNull(message = "Year is required")
    private Integer year;

    @NotNull(message = "Month is required")
    private Integer month;
}