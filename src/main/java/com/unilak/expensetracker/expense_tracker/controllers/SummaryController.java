package com.unilak.expensetracker.expense_tracker.controllers;

import com.unilak.expensetracker.expense_tracker.entities.Transaction;
import com.unilak.expensetracker.expense_tracker.payloads.response.ApiResponse;
import com.unilak.expensetracker.expense_tracker.repositories.TransactionRepository;
import com.unilak.expensetracker.expense_tracker.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for summary endpoints
 * @author YourName
 * @reg YourRegistrationNumber
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Summary", description = "Financial summary operations")
public class SummaryController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Get summary of income and expenses
     * @return Summary data
     */
    @GetMapping("/summary")
    @Operation(summary = "Get financial summary", description = "Retrieve a summary of income and expenses",
    security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSummary() {
        // Get current user email from security context
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find user ID based on email
        Long userId = getUserIdFromEmail(email);

        // Get all transactions for the user
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        // Calculate totals
        double totalIncome = transactions.stream()
                .filter(t -> "income".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = transactions.stream()
                .filter(t -> "expense".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double balance = totalIncome - totalExpense;

        Map<String, Object> summaryData = new HashMap<>();
        summaryData.put("total_income", totalIncome);
        summaryData.put("total_expenses", totalExpense);
        summaryData.put("balance", balance);

        return ResponseEntity.ok(ApiResponse.success(summaryData));
    }

    // Helper method to get user ID from email
    private Long getUserIdFromEmail(String email) {
        // This would be replaced with your actual implementation to find user ID by email
        return 1L; // Placeholder
    }
}