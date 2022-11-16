package com.thesnoozingturtle.moneymanagerrestapi.controller;

import com.thesnoozingturtle.moneymanagerrestapi.config.AppConstants;
import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import com.thesnoozingturtle.moneymanagerrestapi.payload.IncomeResponse;
import com.thesnoozingturtle.moneymanagerrestapi.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<IncomeResponse> getAllIncomes(@PathVariable long userId,
                                                        @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
                                                        @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                                        @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                                        @RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        IncomeResponse incomes = this.incomeService.getAllIncomes(userId, pageNumber, pageSize, sortBy, sortOrder);
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

    @DeleteMapping("/user/{userId}/incomes")
    public ResponseEntity<ApiResponse> deleteAllIncomes(@PathVariable long userId) {
        this.incomeService.deleteAllIncomes(userId);
        return new ResponseEntity<>(new ApiResponse("All incomes of user with user id:" + userId + " deleted successfully!",
                true), HttpStatus.OK);
    }
}
