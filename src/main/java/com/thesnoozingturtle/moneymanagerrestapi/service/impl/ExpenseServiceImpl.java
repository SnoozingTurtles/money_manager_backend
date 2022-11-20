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

    //Method to add expense for a particular user
    @Override
    public ExpenseDto addExpense(ExpenseDto expenseDto, long userId) {
        User user = getUser(userId);
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

    //Method to update expense for a particular user
    @Override
    public ExpenseDto updateExpense(long userId, long expenseId, ExpenseDto expenseDto) {
        User user = getUser(userId);
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

    //Method to get an expense by its id for a particular user
    @Override
    public ExpenseDto getExpenseById(long userId, long expenseId) {
        User user = getUser(userId);
        Expense expenseByIdAndUser = this.expensesRepo.getExpenseByIdAndUser(expenseId, user);
        return this.modelMapper.map(expenseByIdAndUser, ExpenseDto.class);
    }

    //Method to get all the expenses of a user
    @Override
    public ExpenseResponse getAllExpenses(long userId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        User user = getUser(userId);
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Expense> expensePage = this.expensesRepo.getExpensesByUser(user, pageable);
        ExpenseResponse expenseResponse = getExpenseResponse(expensePage);
        return expenseResponse;
    }

    //Method to delete an expense of a user by expense id
    @Override
    public void deleteExpense(long userId, long expenseId) {
        User user = getUser(userId);
        Expense expense = this.expensesRepo.getExpenseByIdAndUser(expenseId, user);
        this.expensesRepo.delete(expense);
    }

    //Method to delete all the expenses of a user
    @Override
    public void deleteAllExpenses(long userId) {
        User user = getUser(userId);
        Set<Expense> expenses = this.expensesRepo.getExpensesByUser(user);
        this.expensesRepo.deleteAll(expenses);
    }

    //Method to get expenses by providing a particular month and year
    @Override
    public Set<ExpenseDto> getAllExpensesByMonthAndYear(long userId, int month, int year) {
        User user = getUser(userId);
        Set<Expense> expenses = this.expensesRepo.getExpensesByMonthAndYearAndUser(user, month, year);
        Set<ExpenseDto> expenseDtos = expenses.stream()
                .map(expense -> this.modelMapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toSet());
        return expenseDtos;
    }

    //Method to get expenses between a particular date
    @Override
    public ExpenseResponse getAllExpensesBetweenAParticularDate(String startDateStr, String endDateStr, long userId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        User user = getUser(userId);
        LocalDateTime startDate = LocalDateTime.parse(startDateStr);
        LocalDateTime endDate;
        if(endDateStr != null) {
            endDate = LocalDateTime.parse(endDateStr);
        } else {
            endDate = LocalDateTime.now();
        }
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Expense> expensePage = this.expensesRepo.getExpensesByDateAddedBetweenAndUser(startDate, endDate, user, pageable);
        ExpenseResponse expenseResponse = getExpenseResponse(expensePage);
        return expenseResponse;
    }

    //Get the user from userId
    private User getUser(long userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        return user;
    }

    //Get the Pageable object from parameters
    private Pageable getPageable(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = (sortOrder.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return pageable;
    }

    //Generate an object of ExpenseResponse
    private ExpenseResponse getExpenseResponse(Page<Expense> expensePage) {
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
}
