package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Expense;
import com.thesnoozingturtle.moneymanagerrestapi.payload.PaginationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;


public interface ExpenseService {
    ExpenseDto addExpense(ExpenseDto expenseDto, String userId, MultipartFile image);
    ExpenseDto updateExpense(String userId, String expenseId, ExpenseDto expenseDto, MultipartFile image);
    ExpenseDto getExpenseById(String userId, String expenseId);
    PaginationResponse<ExpenseDto, Expense> getAllExpenses(String userId, int pageNumber, int pageSize, String sortBy, String sortOrder);
    void deleteExpense(String userId, String expenseId);
    void deleteAllExpenses(String userId);
    Set<ExpenseDto> getAllExpensesByMonthAndYear(String userId, int month, int year);
    PaginationResponse<ExpenseDto, Expense> getAllExpensesBetweenAParticularDate(String startDateStr, String endDateStr, String userId, int pageNumber, int pageSize, String sortBy, String sortOrder);

}
