package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ExpenseResponse;

import java.util.Set;


public interface ExpenseService {
    ExpenseDto addExpense(ExpenseDto expenseDto, long userId);
    ExpenseDto updateExpense(long userId, long expenseId, ExpenseDto expenseDto);
    ExpenseDto getExpenseById(long userId, long expenseId);
    ExpenseResponse getAllExpenses(long userId, int pageNumber, int pageSize, String sortBy, String sortOrder);
    void deleteExpense(long userId, long expenseId);
    void deleteAllExpenses(long userId);
    Set<ExpenseDto> getAllExpensesByMonthAndYear(long userId, int month, int year);
    ExpenseResponse getAllExpensesBetweenAParticularDate(String startDateStr, String endDateStr, long userId, int pageNumber, int pageSize, String sortBy, String sortOrder);

}
