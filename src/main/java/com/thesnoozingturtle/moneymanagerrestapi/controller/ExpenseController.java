package com.thesnoozingturtle.moneymanagerrestapi.controller;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import com.thesnoozingturtle.moneymanagerrestapi.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                                 @RequestBody ExpenseDto expenseDto) {
        ExpenseDto addedExpense = this.expenseService.addExpense(expenseDto, userId);
        return new ResponseEntity<>(addedExpense, HttpStatus.CREATED);
    }
    @GetMapping("/user/{userId}/expenses")
    public ResponseEntity<Set<ExpenseDto>> getAllExpenses(@PathVariable long userId) {
        Set<ExpenseDto> expenses = this.expenseService.getAllExpenses(userId);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/expenses/{expenseId}")
    public ResponseEntity<ExpenseDto> getSingleExpense(@PathVariable long userId, @PathVariable long expenseId) {
        ExpenseDto expenseById = this.expenseService.getExpenseById(userId, expenseId);
        return new ResponseEntity<>(expenseById, HttpStatus.OK);
    }

    @PutMapping("/user/{userId}/expenses/{expenseId}")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable long userId, @PathVariable long expenseId,
                                                    @RequestBody ExpenseDto expenseDto) {
        ExpenseDto updateExpense = this.expenseService.updateExpense(userId, expenseId, expenseDto);
        return new ResponseEntity<>(updateExpense, HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/expenses/{expenseId}")
    public ResponseEntity<ApiResponse> deleteExpense(@PathVariable long userId, @PathVariable long expenseId) {
        this.expenseService.deleteExpense(userId, expenseId);
        return new ResponseEntity<>(new ApiResponse("Expense with expense id " + expenseId + " deleted successfully!", true),
                HttpStatus.OK);
    }
}
