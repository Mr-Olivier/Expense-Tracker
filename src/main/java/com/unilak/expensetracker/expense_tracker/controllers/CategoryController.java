package com.unilak.expensetracker.expense_tracker.controllers;

import com.unilak.expensetracker.expense_tracker.entities.Category;
import com.unilak.expensetracker.expense_tracker.payloads.request.CategoryRequest;
import com.unilak.expensetracker.expense_tracker.payloads.response.ApiResponse;
import com.unilak.expensetracker.expense_tracker.repositories.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for category operations
 * @author YourName
 * @reg YourRegistrationNumber
 */
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Category management operations")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get all categories
     * @return List of categories
     */
    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve all available categories")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /**
     * Get categories by type
     * @param type Category type (income or expense)
     * @return List of categories
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "Get categories by type", description = "Retrieve categories filtered by type (income or expense)")
    public ResponseEntity<ApiResponse<List<Category>>> getCategoriesByType(@PathVariable String type) {
        if (!type.equals("income") && !type.equals("expense")) {
            throw new RuntimeException("Type must be either 'income' or 'expense'");
        }

        List<Category> categories = categoryRepository.findByType(type);
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /**
     * Create a new category
     * @param categoryRequest Category data
     * @return Created category
     */
    @PostMapping
    @Operation(summary = "Create category", description = "Create a new transaction category")
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        // Check if category name already exists
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }

        // Validate type
        if (!categoryRequest.getType().equals("income") && !categoryRequest.getType().equals("expense")) {
            throw new RuntimeException("Type must be either 'income' or 'expense'");
        }

        // Create category
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setType(categoryRequest.getType());

        // Save category
        Category savedCategory = categoryRepository.save(category);

        return ResponseEntity.ok(ApiResponse.success(savedCategory));
    }
}