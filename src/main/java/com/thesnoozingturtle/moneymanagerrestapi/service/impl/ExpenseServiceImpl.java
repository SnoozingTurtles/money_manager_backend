package com.thesnoozingturtle.moneymanagerrestapi.service.impl;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Expense;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import com.thesnoozingturtle.moneymanagerrestapi.exception.UserNotFoundException;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.ExpensesRepo;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.UserRepo;
import com.thesnoozingturtle.moneymanagerrestapi.service.ExpenseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpensesRepo expensesRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public ExpenseServiceImpl(ExpensesRepo expensesRepo, UserRepo userRepo, ModelMapper modelMapper) {
        this.expensesRepo = expensesRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }
    @Override
    public ExpenseDto addExpense(ExpenseDto expenseDto, long userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Expense expense = this.modelMapper.map(expenseDto, Expense.class);
        expense.setUser(user);
        expense.setDateAdded(new Date(System.currentTimeMillis()));
        expense.setImageName("Default.png");
        Expense savedExpense = this.expensesRepo.save(expense);
        return this.modelMapper.map(savedExpense, ExpenseDto.class);
    }

    @Override
    public ExpenseDto updateExpense(long userId, long expenseId, ExpenseDto expenseDto) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Expense expenseByIdAndUser = this.expensesRepo.getExpenseByIdAndUser(expenseId, user);
        expenseByIdAndUser.setAmount(expenseDto.getAmount());
        expenseByIdAndUser.setCategory(expenseDto.getCategory());
        expenseByIdAndUser.setDescription(expenseDto.getDescription());
        expenseByIdAndUser.setType(expenseDto.getType());
        Expense updatedExpense = this.expensesRepo.save(expenseByIdAndUser);
        return this.modelMapper.map(updatedExpense, ExpenseDto.class);
    }

    @Override
    public ExpenseDto getExpenseById(long userId, long expenseId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Expense expenseByIdAndUser = this.expensesRepo.getExpenseByIdAndUser(expenseId, user);
        return this.modelMapper.map(expenseByIdAndUser, ExpenseDto.class);
    }

    @Override
    public Set<ExpenseDto> getAllExpenses(long userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Set<Expense> expensesByUser = this.expensesRepo.getExpensesByUser(user);
        Set<ExpenseDto> allExpenses = expensesByUser
                .stream()
                .map(expense -> this.modelMapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toSet());
        return allExpenses;
    }

    @Override
    public void deleteExpense(long userId, long expenseId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Expense expense = this.expensesRepo.getExpenseByIdAndUser(expenseId, user);
        this.expensesRepo.delete(expense);
    }
}
