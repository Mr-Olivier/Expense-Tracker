package com.unilak.expensetracker.expense_tracker.repositories;

import com.unilak.expensetracker.expense_tracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for User entity
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by email
    Optional<User> findByEmail(String email);

    // Check if a user exists with the given email
    Boolean existsByEmail(String email);
}