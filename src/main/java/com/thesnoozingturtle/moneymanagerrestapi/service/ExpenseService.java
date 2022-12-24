package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Expense;
import com.thesnoozingturtle.moneymanagerrestapi.payload.PaginationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;


public interface ExpenseService {
    ExpenseDto addExpense(ExpenseDto expenseDto, long userId, MultipartFile image);
    ExpenseDto updateExpense(long userId, long expenseId, ExpenseDto expenseDto, MultipartFile image);
    ExpenseDto getExpenseById(long userId, long expenseId);
    PaginationResponse<ExpenseDto, Expense> getAllExpenses(long userId, int pageNumber, int pageSize, String sortBy, String sortOrder);
    void deleteExpense(long userId, long expenseId);
    void deleteAllExpenses(long userId);
    Set<ExpenseDto> getAllExpensesByMonthAndYear(long userId, int month, int year);
    PaginationResponse<ExpenseDto, Expense> getAllExpensesBetweenAParticularDate(String startDateStr, String endDateStr, long userId, int pageNumber, int pageSize, String sortBy, String sortOrder);

}
