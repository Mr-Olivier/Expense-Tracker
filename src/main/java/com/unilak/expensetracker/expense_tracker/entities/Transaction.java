package com.unilak.expensetracker.expense_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Transaction entity
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String type; // income or expense

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column
    private String description;
}