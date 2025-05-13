package com.unilak.expensetracker.expense_tracker.repositories;

import com.unilak.expensetracker.expense_tracker.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for Category entity
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByType(String type);
    boolean existsByName(String name);
}