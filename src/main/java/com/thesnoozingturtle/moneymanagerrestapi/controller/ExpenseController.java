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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/user/{userId}/expenses")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ExpenseDto> addExpense(@PathVariable String userId,
                                                 @Valid @RequestPart("expense") ExpenseDto expenseDto,
                                                 @RequestParam(value = "image", required = false) MultipartFile image) {
        ExpenseDto addedExpense = this.expenseService.addExpense(expenseDto, userId, image);
        return new ResponseEntity<>(addedExpense, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}/expenses")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<PaginationResponse<ExpenseDto, Expense>> getAllExpenses(@PathVariable String userId,
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

    @GetMapping("/user/{userId}/month/{month}/year/{year}/expenses")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<Set<ExpenseDto>> getAllExpensesByMonthAndYear(@PathVariable String userId,
                                                                        @PathVariable int month,
                                                                        @PathVariable int year) {
        Set<ExpenseDto> allExpensesOfLastMonth = this.expenseService.getAllExpensesByMonthAndYear(userId, month, year);
        return new ResponseEntity<>(allExpensesOfLastMonth, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/expenses/{expenseId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ExpenseDto> getSingleExpense(@PathVariable String userId, @PathVariable String expenseId) {
        ExpenseDto expenseById = this.expenseService.getExpenseById(userId, expenseId);
        return new ResponseEntity<>(expenseById, HttpStatus.OK);
    }

    @PutMapping("/user/{userId}/expenses/{expenseId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable String userId, @PathVariable String expenseId,
                                                    @Valid @RequestPart("expense") ExpenseDto expenseDto,
                                                    @RequestParam(value = "image", required = false) MultipartFile image) {
        ExpenseDto updateExpense = this.expenseService.updateExpense(userId, expenseId, expenseDto, image);
        return new ResponseEntity<>(updateExpense, HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/expenses/{expenseId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ApiResponse> deleteExpense(@PathVariable String userId, @PathVariable String expenseId) {
        this.expenseService.deleteExpense(userId, expenseId);
        return new ResponseEntity<>(new ApiResponse("Expense with expense id " + expenseId + " deleted successfully!", true),
                HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/expenses")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ApiResponse> deleteAllExpenses(@PathVariable String userId) {
        this.expenseService.deleteAllExpenses(userId);
        return new ResponseEntity<>(new ApiResponse("All expenses of user with user id:" + userId + " deleted successfully!",
                true), HttpStatus.OK);
    }

}
