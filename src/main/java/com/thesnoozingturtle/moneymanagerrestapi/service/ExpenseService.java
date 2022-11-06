package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import java.util.Set;

public interface ExpenseService {
    ExpenseDto addExpense(ExpenseDto expenseDto, long userId);
    ExpenseDto updateExpense(long userId, long expenseId, ExpenseDto expenseDto);
    ExpenseDto getExpenseById(long userId, long expenseId);
    Set<ExpenseDto> getAllExpenses(long userId);
    void deleteExpense(long userId, long expenseId);
}
