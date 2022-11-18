package com.thesnoozingturtle.moneymanagerrestapi.service.impl;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Expense;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import com.thesnoozingturtle.moneymanagerrestapi.exception.UserNotFoundException;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ExpenseResponse;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.ExpensesRepo;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.UserRepo;
import com.thesnoozingturtle.moneymanagerrestapi.service.ExpenseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        user.setBalance(String.valueOf(Double.parseDouble(user.getBalance()) - Double.parseDouble(expense.getAmount())));
        expense.setUser(user);
        LocalDateTime ldt = LocalDateTime.parse(expenseDto.getDateAdded());
        expense.setDateAdded(ldt);
        expense.setImageName("Default.png");
        this.userRepo.save(user);
        Expense savedExpense = this.expensesRepo.save(expense);
        return this.modelMapper.map(savedExpense, ExpenseDto.class);
    }

    @Override
    public ExpenseDto updateExpense(long userId, long expenseId, ExpenseDto expenseDto) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Expense expenseByIdAndUser = this.expensesRepo.getExpenseByIdAndUser(expenseId, user);

        //to update the balance in user table
        double prevExpenseAmount = Double.parseDouble(expenseByIdAndUser.getAmount());
        double prevBalance = Double.parseDouble(user.getBalance());
        double newBalance = prevBalance + prevExpenseAmount - Double.parseDouble(expenseDto.getAmount());
        user.setBalance(String.valueOf(newBalance));
        this.userRepo.save(user);

        //updating all the fields
        expenseByIdAndUser.setAmount(expenseDto.getAmount());
        expenseByIdAndUser.setCategory(expenseDto.getCategory());
        expenseByIdAndUser.setDescription(expenseDto.getDescription());
        expenseByIdAndUser.setType(expenseDto.getType());
        LocalDateTime ldt = LocalDateTime.parse(expenseDto.getDateAdded());
        expenseByIdAndUser.setDateAdded(ldt);
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
    public ExpenseResponse getAllExpenses(long userId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Sort sort = (sortOrder.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Expense> expensePage = this.expensesRepo.getExpensesByUser(user, pageable);
        List<Expense> expensesByUser = expensePage.getContent();
        List<ExpenseDto> allExpenses = expensesByUser
                .stream()
                .map(expense -> this.modelMapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toList());

        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setExpenses(allExpenses);
        expenseResponse.setLastPage(expensePage.isLast());
        expenseResponse.setPageNumber(expensePage.getNumber());
        expenseResponse.setTotalElements(expensePage.getTotalElements());
        expenseResponse.setTotalPages(expensePage.getTotalPages());
        expenseResponse.setNumberOfElementsOnSinglePage(expensePage.getNumberOfElements());
        return expenseResponse;
    }

    @Override
    public void deleteExpense(long userId, long expenseId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Expense expense = this.expensesRepo.getExpenseByIdAndUser(expenseId, user);
        this.expensesRepo.delete(expense);
    }

    @Override
    public void deleteAllExpenses(long userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Set<Expense> expenses = this.expensesRepo.getExpensesByUser(user);
        this.expensesRepo.deleteAll(expenses);
    }
}
