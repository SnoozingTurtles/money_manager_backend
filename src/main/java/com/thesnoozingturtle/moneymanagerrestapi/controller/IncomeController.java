package com.thesnoozingturtle.moneymanagerrestapi.controller;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import com.thesnoozingturtle.moneymanagerrestapi.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class IncomeController {
    private final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping("/user/{userId}/incomes")
    public ResponseEntity<IncomeDto> addIncome(@PathVariable long userId,
                                                @RequestBody IncomeDto incomeDto) {
        IncomeDto income = this.incomeService.addIncome(userId, incomeDto);
        return new ResponseEntity<>(income, HttpStatus.CREATED);
    }
    @GetMapping("/user/{userId}/incomes")
    public ResponseEntity<Set<IncomeDto>> getAllIncomes(@PathVariable long userId) {
        Set<IncomeDto> incomes = this.incomeService.getAllIncomes(userId);
        return new ResponseEntity<>(incomes, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/incomes/{incomeId}")
    public ResponseEntity<IncomeDto> getSingleIncome(@PathVariable long userId, @PathVariable long incomeId) {
        IncomeDto incomeDto = this.incomeService.getIncomeById(userId, incomeId);
        return new ResponseEntity<>(incomeDto, HttpStatus.OK);
    }

    @PutMapping("/user/{userId}/incomes/{incomeId}")
    public ResponseEntity<IncomeDto> updateIncome(@PathVariable long userId, @PathVariable long incomeId,
                                                    @RequestBody IncomeDto incomeDto) {
        IncomeDto updateIncome = this.incomeService.updateIncome(userId, incomeId, incomeDto);
        return new ResponseEntity<>(updateIncome, HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/incomes/{incomeId}")
    public ResponseEntity<ApiResponse> deleteIncome(@PathVariable long userId, @PathVariable long incomeId) {
        this.incomeService.deleteIncome(userId, incomeId);
        return new ResponseEntity<>(new ApiResponse("Income with income id " + incomeId + " deleted successfully!", true),
                HttpStatus.OK);
    }
}
