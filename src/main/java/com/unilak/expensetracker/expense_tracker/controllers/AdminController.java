

package com.unilak.expensetracker.expense_tracker.controllers;

import com.unilak.expensetracker.expense_tracker.entities.Transaction;
import com.unilak.expensetracker.expense_tracker.entities.User;
import com.unilak.expensetracker.expense_tracker.exceptions.ResourceNotFoundException;
import com.unilak.expensetracker.expense_tracker.payloads.response.ApiResponse;
import com.unilak.expensetracker.expense_tracker.repositories.TransactionRepository;
import com.unilak.expensetracker.expense_tracker.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for admin operations
 * @author YourName
 * @reg YourRegistrationNumber
 */
@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "Admin operations")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Get all users
     * @return List of all users
     */
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Admin can list all registered users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * Ban or activate a user
     * @param id User ID
     * @param statusRequest Status request (active or banned)
     * @return Updated user
     */
    @PatchMapping("/users/{id}/status")
    @Operation(summary = "Ban or activate user", description = "Admin can ban or activate a user account")
    public ResponseEntity<ApiResponse<User>> updateUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusRequest) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        String status = statusRequest.get("status");
        if (status == null || (!status.equals("active") && !status.equals("banned"))) {
            throw new IllegalArgumentException("Status must be either 'active' or 'banned'");
        }

        user.setStatus(status);
        User updatedUser = userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    /**
     * Get transaction report grouped by category
     * @return Report data
     */
    @GetMapping("/analytics/transactions")
    @Operation(summary = "Get transaction report", description = "Generate a summary report of transactions grouped by category")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTransactionReport() {
        // Fetch all transactions
        List<Transaction> allTransactions = transactionRepository.findAll();

        if (allTransactions.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(Collections.emptyList()));
        }

        // Group transactions by category
        Map<String, List<Transaction>> transactionsByCategory = allTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory));

        // Create report data
        List<Map<String, Object>> reportData = new ArrayList<>();

        transactionsByCategory.forEach((category, transactions) -> {
            Map<String, Object> categoryReport = new HashMap<>();
            categoryReport.put("category", category);

            double totalIncome = transactions.stream()
                    .filter(t -> "income".equals(t.getType()))
                    .mapToDouble(Transaction::getAmount)
                    .sum();

            double totalExpense = transactions.stream()
                    .filter(t -> "expense".equals(t.getType()))
                    .mapToDouble(Transaction::getAmount)
                    .sum();

            categoryReport.put("total_income", totalIncome);
            categoryReport.put("total_expense", totalExpense);

            reportData.add(categoryReport);
        });

        return ResponseEntity.ok(ApiResponse.success(reportData));
    }
}