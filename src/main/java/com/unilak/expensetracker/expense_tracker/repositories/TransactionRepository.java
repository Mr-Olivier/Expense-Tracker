package com.unilak.expensetracker.expense_tracker.repositories;

import com.unilak.expensetracker.expense_tracker.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Transaction entity
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdAndCategory(Long userId, String category);

    List<Transaction> findByUserIdAndDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByUserIdAndCategoryAndDateBetween(
            Long userId, String category, LocalDateTime startDate, LocalDateTime endDate);


}