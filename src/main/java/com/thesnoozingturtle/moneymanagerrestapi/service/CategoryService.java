package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto, String userId);
    List<CategoryDto> getAllCategoriesByUserId(String userId);
    CategoryDto getCategoryByUserIdAndCategoryId(String userId, String categoryId);
    void deleteCategoryByUserIdAndCategoryId(String userId, String categoryId);
}
