package com.thesnoozingturtle.moneymanagerrestapi.controller;

import com.thesnoozingturtle.moneymanagerrestapi.dto.CategoryDto;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import com.thesnoozingturtle.moneymanagerrestapi.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/users/{userId}/categories")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<List<CategoryDto>> getAllCategoriesByUser(@PathVariable String userId) {
        List<CategoryDto> allCategoriesByUserId = categoryService.getAllCategoriesByUserId(userId);
        return ResponseEntity.ok(allCategoriesByUserId);
    }

    @GetMapping("/users/{userId}/categories/{categoryId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<CategoryDto> getCategoryByUserIdAndCategoryId(@PathVariable String userId, @PathVariable String categoryId) {
        CategoryDto categoryByUserIdAndCategoryId = categoryService.getCategoryByUserIdAndCategoryId(userId, categoryId);
        return ResponseEntity.ok(categoryByUserIdAndCategoryId);
    }

    @PostMapping("/users/{userId}/categories")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ApiResponse> createCategory(@PathVariable String userId, @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.createCategory(categoryDto, userId);
        URI categoryUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{categoryId}")
                .buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(categoryUri).body(ApiResponse.builder().success(true).message("Category created successfully!").build());
    }

    @DeleteMapping("/users/{userId}/categories/{categoryId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String userId, @PathVariable String categoryId) {
        categoryService.deleteCategoryByUserIdAndCategoryId(userId, categoryId);
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Category deleted successfully!").build());
    }
}
