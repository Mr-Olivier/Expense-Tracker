

package com.unilak.expensetracker.expense_tracker.controllers;

import com.unilak.expensetracker.expense_tracker.entities.Transaction;
import com.unilak.expensetracker.expense_tracker.entities.User;
import com.unilak.expensetracker.expense_tracker.exceptions.ResourceNotFoundException;
import com.unilak.expensetracker.expense_tracker.exceptions.UnauthorizedException;
import com.unilak.expensetracker.expense_tracker.payloads.request.TransactionRequest;
import com.unilak.expensetracker.expense_tracker.payloads.response.ApiResponse;
import com.unilak.expensetracker.expense_tracker.repositories.TransactionRepository;
import com.unilak.expensetracker.expense_tracker.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for transaction operations
 * @author YourName
 * @reg YourRegistrationNumber
 */
@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transactions", description = "Transaction operations")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new transaction
     * @param transactionRequest Transaction data
     * @return Created transaction
     */
    @PostMapping
    @Operation(summary = "Add a new transaction", description = "Creates a new income or expense entry")
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        // Validate transaction type
        if (!transactionRequest.getType().equals("income") && !transactionRequest.getType().equals("expense")) {
            throw new IllegalArgumentException("Transaction type must be either 'income' or 'expense'");
        }

        // Get current logged-in user
        User user = getCurrentUser();

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setUserId(user.getId());
        transaction.setType(transactionRequest.getType());
        transaction.setCategory(transactionRequest.getCategory());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setDate(LocalDateTime.now());

        // Save transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        return ResponseEntity.ok(ApiResponse.success(savedTransaction));
    }

    /**
     * Get all transactions for the current user
     * @return List of transactions
     */
    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieves all transactions for the logged-in user")
    public ResponseEntity<ApiResponse<List<Transaction>>> getAllTransactions() {
        // Get current logged-in user
        User user = getCurrentUser();

        // Get transactions for user
        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());

        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    /**
     * Get a specific transaction
     * @param id Transaction ID
     * @return Transaction details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get transaction details", description = "Retrieve details of a specific transaction")
    public ResponseEntity<ApiResponse<Transaction>> getTransaction(@PathVariable Long id) {
        // Get current logged-in user
        User user = getCurrentUser();

        // Find transaction
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));

        // Check if transaction belongs to user
        if (!transaction.getUserId().equals(user.getId())) {
            throw new UnauthorizedException("You don't have permission to access this transaction");
        }

        return ResponseEntity.ok(ApiResponse.success(transaction));
    }

    /**
     * Update a transaction
     * @param id Transaction ID
     * @param transactionRequest Updated transaction data
     * @return Updated transaction
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Update transaction", description = "Update details of a specific transaction")
    public ResponseEntity<ApiResponse<Transaction>> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest transactionRequest) {

        // Validate transaction type
        if (!transactionRequest.getType().equals("income") && !transactionRequest.getType().equals("expense")) {
            throw new IllegalArgumentException("Transaction type must be either 'income' or 'expense'");
        }

        // Get current logged-in user
        User user = getCurrentUser();

        // Find transaction
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));

        // Check if transaction belongs to user
        if (!transaction.getUserId().equals(user.getId())) {
            throw new UnauthorizedException("You don't have permission to update this transaction");
        }

        // Update transaction
        transaction.setType(transactionRequest.getType());
        transaction.setCategory(transactionRequest.getCategory());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());

        // Save updated transaction
        Transaction updatedTransaction = transactionRepository.save(transaction);

        return ResponseEntity.ok(ApiResponse.success(updatedTransaction));
    }

    /**
     * Delete a transaction
     * @param id Transaction ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction", description = "Delete a specific transaction")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteTransaction(@PathVariable Long id) {
        // Get current logged-in user
        User user = getCurrentUser();

        // Find transaction
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));

        // Check if transaction belongs to user
        if (!transaction.getUserId().equals(user.getId())) {
            throw new UnauthorizedException("You don't have permission to delete this transaction");
        }

        // Delete transaction
        transactionRepository.delete(transaction);

        // Create response message
        Map<String, String> response = new HashMap<>();
        response.put("message", "Transaction deleted successfully");

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Filter transactions by date range and/or category
     * @param startDate Start date (yyyy-MM-dd)
     * @param endDate End date (yyyy-MM-dd)
     * @param category Category name
     * @return Filtered transactions
     */
    @GetMapping("/filter")
    @Operation(summary = "Filter transactions", description = "Filter transactions by date range and/or category")
    public ResponseEntity<ApiResponse<List<Transaction>>> filterTransactions(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String category) {

        // Get current logged-in user
        User user = getCurrentUser();

        Long userId = user.getId();
        List<Transaction> filteredTransactions;

        try {
            // Filter by date and category
            if (startDate != null && endDate != null && category != null) {
                LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T00:00:00");
                LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T23:59:59");
                filteredTransactions = transactionRepository.findByUserIdAndCategoryAndDateBetween(userId, category, startDateTime, endDateTime);
            }
            // Filter by date only
            else if (startDate != null && endDate != null) {
                LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T00:00:00");
                LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T23:59:59");
                filteredTransactions = transactionRepository.findByUserIdAndDateBetween(userId, startDateTime, endDateTime);
            }
            // Filter by category only
            else if (category != null) {
                filteredTransactions = transactionRepository.findByUserIdAndCategory(userId, category);
            }
            // No filters, return all transactions
            else {
                filteredTransactions = transactionRepository.findByUserId(userId);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error filtering transactions: " + e.getMessage());
        }

        return ResponseEntity.ok(ApiResponse.success(filteredTransactions));
    }

    /**
     * Get transaction summary by type
     * @return Summary of income and expenses
     */
    @GetMapping("/summary")
    @Operation(summary = "Get transaction summary", description = "Get a summary of income and expenses")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTransactionSummary() {
        // Get current logged-in user
        User user = getCurrentUser();

        // Get all transactions for user
        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());

        // Calculate total income
        double totalIncome = transactions.stream()
                .filter(t -> "income".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        // Calculate total expenses
        double totalExpenses = transactions.stream()
                .filter(t -> "expense".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        // Calculate balance
        double balance = totalIncome - totalExpenses;

        // Prepare response
        Map<String, Object> summary = new HashMap<>();
        summary.put("total_income", totalIncome);
        summary.put("total_expenses", totalExpenses);
        summary.put("balance", balance);

        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    /**
     * Helper method to get the current authenticated user
     * @return User entity
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}