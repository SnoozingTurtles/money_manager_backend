package com.thesnoozingturtle.moneymanagerrestapi.controller;

import com.thesnoozingturtle.moneymanagerrestapi.config.AppConstants;
import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Expense;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import com.thesnoozingturtle.moneymanagerrestapi.payload.PaginationResponse;
import com.thesnoozingturtle.moneymanagerrestapi.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/user/{userId}/expenses")
    public ResponseEntity<ExpenseDto> addExpense(@PathVariable long userId,
                                                 @Valid @RequestBody ExpenseDto expenseDto) {
        ExpenseDto addedExpense = this.expenseService.addExpense(expenseDto, userId);
        return new ResponseEntity<>(addedExpense, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}/expenses")
    public ResponseEntity<PaginationResponse<ExpenseDto, Expense>> getAllExpenses(@PathVariable long userId,
                                                                                  @RequestParam(value = "startDate", required = false) String startDateStr,
                                                                                  @RequestParam(value = "endDate", required = false) String endDateStr,
                                                                                  @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
                                                                                  @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                                                                  @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                                                                  @RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        PaginationResponse<ExpenseDto, Expense> paginationResponse;
        if (startDateStr == null || startDateStr.isEmpty()) {
            paginationResponse = this.expenseService.getAllExpenses(userId, pageNumber, pageSize, sortBy, sortOrder);
        } else if(endDateStr != null) {
            paginationResponse = this.expenseService.getAllExpensesBetweenAParticularDate(startDateStr, endDateStr, userId, pageNumber, pageSize, sortBy, sortOrder);
        } else {
            paginationResponse = this.expenseService.getAllExpensesBetweenAParticularDate(startDateStr, null, userId, pageNumber, pageSize, sortBy, sortOrder);
        }
        return new ResponseEntity<>(paginationResponse, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/expenses/{expenseId}")
    public ResponseEntity<ExpenseDto> getSingleExpense(@PathVariable long userId, @PathVariable long expenseId) {
        ExpenseDto expenseById = this.expenseService.getExpenseById(userId, expenseId);
        return new ResponseEntity<>(expenseById, HttpStatus.OK);
    }

    @PutMapping("/user/{userId}/expenses/{expenseId}")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable long userId, @PathVariable long expenseId,
                                                    @Valid @RequestBody ExpenseDto expenseDto) {
        ExpenseDto updateExpense = this.expenseService.updateExpense(userId, expenseId, expenseDto);
        return new ResponseEntity<>(updateExpense, HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/expenses/{expenseId}")
    public ResponseEntity<ApiResponse> deleteExpense(@PathVariable long userId, @PathVariable long expenseId) {
        this.expenseService.deleteExpense(userId, expenseId);
        return new ResponseEntity<>(new ApiResponse("Expense with expense id " + expenseId + " deleted successfully!", true),
                HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/expenses")
    public ResponseEntity<ApiResponse> deleteAllExpenses(@PathVariable long userId) {
        this.expenseService.deleteAllExpenses(userId);
        return new ResponseEntity<>(new ApiResponse("All expenses of user with user id:" + userId + " deleted successfully!",
                true), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/month/{month}/year/{year}/expenses")
    public ResponseEntity<Set<ExpenseDto>> getAllExpensesByMonthAndYear(@PathVariable long userId,
                                                                        @PathVariable int month,
                                                                        @PathVariable int year) {
        Set<ExpenseDto> allExpensesOfLastMonth = this.expenseService.getAllExpensesByMonthAndYear(userId, month, year);
        return new ResponseEntity<>(allExpensesOfLastMonth, HttpStatus.OK);
    }
}
