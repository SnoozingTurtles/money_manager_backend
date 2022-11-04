package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import java.util.Set;

public interface ExpenseService {
    ExpenseDto addExpense(ExpenseDto expenseDto);
    ExpenseDto updateExpense(long expenseId, ExpenseDto expenseDto);
    ExpenseDto getExpenseById(long expenseId);
    Set<ExpenseDto> getAllExpenses();
    void deleteExpense(int expenseId);
}
