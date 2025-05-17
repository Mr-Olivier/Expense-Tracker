package com.unilak.expensetracker.expense_tracker.controllers;

import com.unilak.expensetracker.expense_tracker.entities.Transaction;
import com.unilak.expensetracker.expense_tracker.entities.User;
import com.unilak.expensetracker.expense_tracker.payloads.request.ReportRequest;
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
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for financial reports
 * @author YourName
 * @reg YourRegistrationNumber
 */
@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports", description = "Financial reporting operations")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Generate monthly financial report
     * @param reportRequest Report parameters (year and month)
     * @return Monthly financial report
     */
    @PostMapping("/monthly")
    @Operation(summary = "Generate monthly report", description = "Generate a financial report for a specific month")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateMonthlyReport(
            @Valid @RequestBody ReportRequest reportRequest) {

        // Get current logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate month and year
        int year = reportRequest.getYear();
        int month = reportRequest.getMonth();

        if (month < 1 || month > 12) {
            throw new RuntimeException("Month must be between 1 and 12");
        }

        // Get start and end of month
        LocalDateTime startOfMonth = YearMonth.of(year, month).atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = YearMonth.of(year, month).atEndOfMonth().plusDays(1).atStartOfDay();

        // Get all transactions for the month
        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(t -> t.getUserId().equals(user.getId()))
                .filter(t -> t.getDate().isAfter(startOfMonth) && t.getDate().isBefore(endOfMonth))
                .collect(Collectors.toList());

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

        // Group expenses by category
        Map<String, Double> expensesByCategory = transactions.stream()
                .filter(t -> "expense".equals(t.getType()))
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        // Group income by category
        Map<String, Double> incomeByCategory = transactions.stream()
                .filter(t -> "income".equals(t.getType()))
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        // Create report data
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("year", year);
        reportData.put("month", Month.of(month).toString());
        reportData.put("total_income", totalIncome);
        reportData.put("total_expenses", totalExpense);
        reportData.put("balance", balance);
        reportData.put("expenses_by_category", expensesByCategory);
        reportData.put("income_by_category", incomeByCategory);
        reportData.put("transactions", transactions);

        return ResponseEntity.ok(ApiResponse.success(reportData));
    }
}