package com.thesnoozingturtle.moneymanagerrestapi.repositories;

import com.thesnoozingturtle.moneymanagerrestapi.entity.Category;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepo extends JpaRepository<Category, UUID> {
    Optional<Category> findCategoryByIdAndUser(UUID id, User user);
    List<Category> findCategoriesByUser(User user);
    void deleteByIdAndUser(UUID id, User user);
}
