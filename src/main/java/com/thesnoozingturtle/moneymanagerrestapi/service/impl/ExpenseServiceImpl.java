package com.thesnoozingturtle.moneymanagerrestapi.service.impl;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Category;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Expense;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import com.thesnoozingturtle.moneymanagerrestapi.exception.EntityNotFoundException;
import com.thesnoozingturtle.moneymanagerrestapi.payload.PaginationResponse;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.CategoryRepo;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.ExpensesRepo;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.UserRepo;
import com.thesnoozingturtle.moneymanagerrestapi.service.ExpenseService;
import com.thesnoozingturtle.moneymanagerrestapi.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpensesRepo expensesRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final CategoryRepo categoryRepo;

    @Autowired
    public ExpenseServiceImpl(ExpensesRepo expensesRepo, UserRepo userRepo, ModelMapper modelMapper, ImageService imageService, CategoryRepo categoryRepo) {
        this.expensesRepo = expensesRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.categoryRepo = categoryRepo;
    }

    //Method to add expense for a particular user
    @Override
    public ExpenseDto addExpense(ExpenseDto expenseDto, String userId, String categoryId) {
        User user = getUser(userId);
        Category category = getCategoryByCategoryIdAndUser(categoryId, user);
        Expense expense = this.modelMapper.map(expenseDto, Expense.class);
        user.setBalance(String.valueOf(Double.parseDouble(user.getBalance()) - Double.parseDouble(expense.getAmount())));
        expense.setUser(user);
        LocalDateTime ldt = LocalDateTime.parse(expenseDto.getDateAdded());
        expense.setDateAdded(ldt);
        expense.setCategory(category);

        this.userRepo.save(user);
        Expense savedExpense = this.expensesRepo.save(expense);
        return this.modelMapper.map(savedExpense, ExpenseDto.class);
    }

    //Method to update expense for a particular user
    @Override
    public ExpenseDto updateExpense(String userId, String expenseId, String categoryId, ExpenseDto expenseDto) {
        User user = getUser(userId);
        Category category = getCategoryByCategoryIdAndUser(categoryId, user);
        Expense expenseByIdAndUser = getExpense(expenseId, user);

        try {
            //to update the balance in user table
            double prevExpenseAmount = Double.parseDouble(expenseByIdAndUser.getAmount());
            double prevBalance = Double.parseDouble(user.getBalance());
            double newBalance = prevBalance + prevExpenseAmount - Double.parseDouble(expenseDto.getAmount());
            user.setBalance(String.valueOf(newBalance));
            this.userRepo.save(user);

            //updating all the fields
            expenseByIdAndUser.setAmount(expenseDto.getAmount());
//            expenseByIdAndUser.setCategory(expenseDto.getCategory());
            expenseByIdAndUser.setDescription(expenseDto.getDescription());
            expenseByIdAndUser.setType(expenseDto.getType());
            LocalDateTime ldt = LocalDateTime.parse(expenseDto.getDateAdded());
            expenseByIdAndUser.setDateAdded(ldt);
            expenseByIdAndUser.setCategory(category);

            Expense updatedExpense = this.expensesRepo.save(expenseByIdAndUser);
            return this.modelMapper.map(updatedExpense, ExpenseDto.class);
        }  catch (Exception e) {
            throw new EntityNotFoundException("No expense found for the given ID!");
        }
    }



    //Method to get an expense by its id for a particular user
    @Override
    public ExpenseDto getExpenseById(String userId, String expenseId) {
        User user = getUser(userId);
        try {
            Expense expenseByIdAndUser = getExpense(expenseId, user);
            return this.modelMapper.map(expenseByIdAndUser, ExpenseDto.class);
        }  catch (Exception e) {
            throw new EntityNotFoundException("No expense found for the given ID!");
        }
    }

    //Method to get all the expenses of a user
    @Override
    public PaginationResponse<ExpenseDto, Expense> getAllExpenses(String userId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        User user = getUser(userId);
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Expense> expensePage = this.expensesRepo.getExpensesByUser(user, pageable);
        PaginationResponse<ExpenseDto, Expense> paginationResponse = getPaginationResponse(expensePage);
        return paginationResponse;
    }

    @Override
    public PaginationResponse<ExpenseDto, Expense> getAllExpensesByCategory(String userId, String categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        User user = getUser(userId);
        Category categoryByCategoryIdAndUser = getCategoryByCategoryIdAndUser(categoryId, user);
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Expense> expensesByUserAndCategory = expensesRepo.getExpensesByUserAndCategory(user, categoryByCategoryIdAndUser, pageable);
        return getPaginationResponse(expensesByUserAndCategory);
    }

    //Method to delete an expense of a user by expense id
    @Override
    public void deleteExpense(String userId, String expenseId) {
        User user = getUser(userId);
        try {
            Expense expense = getExpense(expenseId, user);
            this.expensesRepo.delete(expense);
        }  catch (Exception e) {
            throw new EntityNotFoundException("No expense found for the given ID!");
        }
    }

    //Method to delete all the expenses of a user
    @Override
    public void deleteAllExpenses(String userId) {
        User user = getUser(userId);
        Set<Expense> expenses = this.expensesRepo.getExpensesByUser(user);
        this.expensesRepo.deleteAll(expenses);
    }

    //Method to get expenses by providing a particular month and year
    @Override
    public Set<ExpenseDto> getAllExpensesByMonthAndYear(String userId, int month, int year) {
        User user = getUser(userId);
        Set<Expense> expenses = this.expensesRepo.getExpensesByMonthAndYearAndUser(user, month, year);
        Set<ExpenseDto> expenseDtos = expenses.stream()
                .map(expense -> this.modelMapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toSet());
        return expenseDtos;
    }

    //Method to get expenses by providing a particular month and year
    @Override
    public PaginationResponse<ExpenseDto, Expense> getAllExpensesBetweenAParticularDate(String startDateStr, String endDateStr, String userId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
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
        PaginationResponse<ExpenseDto, Expense> paginationResponse = getPaginationResponse(expensePage);
        return paginationResponse;
    }

    //Get the user from userId
    private User getUser(String userId) {
        User user = this.userRepo.findById(UUID.fromString(userId)).orElseThrow(() -> new EntityNotFoundException("No user exists with the given id!"));
        return user;
    }

    private Category getCategoryByCategoryIdAndUser(String categoryId, User user) {
        return categoryRepo.findCategoryByIdAndUser(UUID.fromString(categoryId), user)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
    }

    //Get the Pageable object from parameters
    private Pageable getPageable(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = (sortOrder.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return pageable;
    }

    //Generate an object of PaginationResponse
    private PaginationResponse<ExpenseDto, Expense> getPaginationResponse(Page<Expense> expensePage) {
        List<Expense> expensesByUser = expensePage.getContent();
        List<ExpenseDto> allExpenses = expensesByUser
                .stream()
                .map(expense -> this.modelMapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toList());

        PaginationResponse<ExpenseDto, Expense> paginationResponse = new PaginationResponse<>(expensePage, allExpenses);
        return paginationResponse;
    }
    private Expense getExpense(String expenseId, User user) {
        Expense expenseByIdAndUser = this.expensesRepo
                .getExpenseByIdAndUser(UUID.fromString(expenseId), user)
                .orElseThrow(() -> new EntityNotFoundException("No expense found with id:" + expenseId));
        return expenseByIdAndUser;
    }
}
