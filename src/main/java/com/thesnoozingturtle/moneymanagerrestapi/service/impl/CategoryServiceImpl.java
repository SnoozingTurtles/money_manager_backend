package com.thesnoozingturtle.moneymanagerrestapi.service.impl;

import com.thesnoozingturtle.moneymanagerrestapi.dto.CategoryDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Category;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import com.thesnoozingturtle.moneymanagerrestapi.exception.EntityNotFoundException;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.CategoryRepo;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.UserRepo;
import com.thesnoozingturtle.moneymanagerrestapi.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;


    public CategoryServiceImpl(CategoryRepo categoryRepo, UserRepo userRepo, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto, String userId) {
        User user = getUser(userId);
        Category category = modelMapper.map(categoryDto, Category.class);
        category.setUser(user);
        Category savedCategory = categoryRepo.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategoriesByUserId(String userId) {
        User user = getUser(userId);
        List<CategoryDto> categories = categoryRepo.findCategoriesByUser(user)
                .stream().map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
        return categories;
    }

    @Override
    public CategoryDto getCategoryByUserIdAndCategoryId(String userId, String categoryId) {
        User user = getUser(userId);
        Category category = categoryRepo.findCategoryByIdAndUser(UUID.fromString(categoryId), user)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id:" + categoryId));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    @Transactional
    public void deleteCategoryByUserIdAndCategoryId(String userId, String categoryId) {
        User user = getUser(userId);
        categoryRepo.deleteByIdAndUser(UUID.fromString(categoryId), user);
    }

    private User getUser(String userId) {
        User user = this.userRepo.findById(UUID.fromString(userId)).orElseThrow(() -> new EntityNotFoundException("No user found with id:" + userId));
        return user;
    }
}
