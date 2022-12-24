package com.thesnoozingturtle.moneymanagerrestapi.controller;

import com.thesnoozingturtle.moneymanagerrestapi.config.AppConstants;
import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Income;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import com.thesnoozingturtle.moneymanagerrestapi.payload.PaginationResponse;
import com.thesnoozingturtle.moneymanagerrestapi.service.IncomeService;
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
public class IncomeController {
    private final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping("/user/{userId}/incomes")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<IncomeDto> addIncome(@PathVariable long userId,
                                               @Valid @RequestPart(value = "income", required = true) IncomeDto incomeDto,
                                               @RequestParam(value = "image", required = false) MultipartFile image) {
        IncomeDto income = this.incomeService.addIncome(userId, incomeDto, image);
        return new ResponseEntity<>(income, HttpStatus.CREATED);
    }
    @GetMapping("/user/{userId}/incomes")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<PaginationResponse<IncomeDto, Income>> getAllIncomes(@PathVariable long userId,
                                                                               @RequestParam(value = "startDate", required = false) String startDateStr,
                                                                               @RequestParam(value = "endDate", required = false) String endDateStr,
                                                                               @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
                                                                               @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                                                               @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                                                               @RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        PaginationResponse<IncomeDto, Income> paginationResponse;
        if (startDateStr == null || startDateStr.isEmpty()) {
            paginationResponse = this.incomeService.getAllIncomes(userId, pageNumber, pageSize, sortBy, sortOrder);
        } else if(endDateStr != null) {
            paginationResponse = this.incomeService.getAllIncomesBetweenAParticularDate(startDateStr, endDateStr, userId, pageNumber, pageSize, sortBy, sortOrder);
        } else {
            paginationResponse = this.incomeService.getAllIncomesBetweenAParticularDate(startDateStr, null, userId, pageNumber, pageSize, sortBy, sortOrder);
        }
        return new ResponseEntity<>(paginationResponse, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/month/{month}/year/{year}/incomes")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<Set<IncomeDto>> getAllIncomesByMonthAndYear(@PathVariable long userId,
                                                                        @PathVariable int month,
                                                                        @PathVariable int year) {
        Set<IncomeDto> allIncomesByMonthAndYear = this.incomeService.getAllIncomesByMonthAndYear(userId, month, year);
        return new ResponseEntity<>(allIncomesByMonthAndYear, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/incomes/{incomeId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<IncomeDto> getSingleIncome(@PathVariable long userId, @PathVariable long incomeId) {
        IncomeDto incomeDto = this.incomeService.getIncomeById(userId, incomeId);
        return new ResponseEntity<>(incomeDto, HttpStatus.OK);
    }

    @PutMapping("/user/{userId}/incomes/{incomeId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<IncomeDto> updateIncome(@PathVariable long userId, @PathVariable long incomeId,
                                                  @Valid @RequestPart("income") IncomeDto incomeDto,
                                                  @RequestParam(value = "image", required = false) MultipartFile image) {
        IncomeDto updateIncome = this.incomeService.updateIncome(userId, incomeId, incomeDto, image);
        return new ResponseEntity<>(updateIncome, HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/incomes/{incomeId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ApiResponse> deleteIncome(@PathVariable long userId, @PathVariable long incomeId) {
        this.incomeService.deleteIncome(userId, incomeId);
        return new ResponseEntity<>(new ApiResponse("Income with income id " + incomeId + " deleted successfully!", true),
                HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/incomes")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ApiResponse> deleteAllIncomes(@PathVariable long userId) {
        this.incomeService.deleteAllIncomes(userId);
        return new ResponseEntity<>(new ApiResponse("All incomes of user with user id:" + userId + " deleted successfully!",
                true), HttpStatus.OK);
    }
}
