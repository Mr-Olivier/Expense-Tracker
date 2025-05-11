package com.unilak.expensetracker.expense_tracker.config;

import com.unilak.expensetracker.expense_tracker.entities.Category;
import com.unilak.expensetracker.expense_tracker.entities.User;
import com.unilak.expensetracker.expense_tracker.repositories.CategoryRepository;
import com.unilak.expensetracker.expense_tracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Initialize data on application startup
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create admin user if not exists
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setPassword(passwordEncoder.encode("admin@123"));
            adminUser.setPhoneNumber("1234567890");
            adminUser.setCreatedOn(LocalDateTime.now());
            adminUser.setStatus("active");
            adminUser.setRole("ADMIN");
            userRepository.save(adminUser);
            System.out.println("Admin user created successfully");
        }

        // Create default categories if not exist
        if (categoryRepository.count() == 0) {
            // Expense categories
            List<Category> expenseCategories = Arrays.asList(
                    new Category(null, "Groceries", "Food and household items", "expense"),
                    new Category(null, "Utilities", "Electricity, water, gas, etc.", "expense"),
                    new Category(null, "Rent", "Housing rent or mortgage", "expense"),
                    new Category(null, "Transportation", "Fuel, public transport, etc.", "expense"),
                    new Category(null, "Entertainment", "Movies, games, dining out, etc.", "expense"),
                    new Category(null, "Healthcare", "Medical expenses", "expense"),
                    new Category(null, "Education", "Tuition, books, courses", "expense"),
                    new Category(null, "Shopping", "Clothing, electronics, etc.", "expense"),
                    new Category(null, "Travel", "Vacations, business trips", "expense"),
                    new Category(null, "Miscellaneous", "Other expenses", "expense")
            );

            // Income categories
            List<Category> incomeCategories = Arrays.asList(
                    new Category(null, "Salary", "Regular employment income", "income"),
                    new Category(null, "Freelance", "Freelance or contract work", "income"),
                    new Category(null, "Investments", "Dividends, interest, etc.", "income"),
                    new Category(null, "Gifts", "Money received as gifts", "income"),
                    new Category(null, "Refunds", "Money refunded", "income"),
                    new Category(null, "Other", "Other sources of income", "income")
            );

            // Save all categories
            categoryRepository.saveAll(expenseCategories);
            categoryRepository.saveAll(incomeCategories);

            System.out.println("Default categories created successfully");
        }
    }
}