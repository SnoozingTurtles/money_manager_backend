package com.thesnoozingturtle.moneymanagerrestapi.repositories;

import com.thesnoozingturtle.moneymanagerrestapi.entity.Expense;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ExpensesRepo extends JpaRepository<Expense, Long> {
    Expense getExpenseByIdAndUser(long expenseId, User user);
    Page<Expense> getExpensesByUser(User user, Pageable pageable);

}
