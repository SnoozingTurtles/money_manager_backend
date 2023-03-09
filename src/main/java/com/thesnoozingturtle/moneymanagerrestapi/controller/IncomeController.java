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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;


@RestController
@RequestMapping("/api/users")
public class IncomeController {
    private final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping("/{userId}/categories/{categoryId}/incomes")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ApiResponse> addIncome(@PathVariable String userId,
                                               @PathVariable String categoryId,
                                               @Valid @RequestBody IncomeDto incomeDto) {
        IncomeDto income = this.incomeService.addIncome(userId, categoryId, incomeDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/api/users/{userId}/incomes/{incomeId}")
                .buildAndExpand(userId, income.getId()).toUri();
        return ResponseEntity.created(uri).body(ApiResponse.builder()
                .success(true)
                .message("Income added successfully!")
                .build());
    }
    @GetMapping("/{userId}/incomes")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<PaginationResponse<IncomeDto, Income>> getAllIncomes(@PathVariable String userId,
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

    @GetMapping("/{userId}/categories/{categoryId}/incomes")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<PaginationResponse<IncomeDto, Income>> getAllIncomesByUserAndCategory(@PathVariable String userId,
                                                                                                @PathVariable String categoryId,
                                                                                                @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
                                                                                                @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                                                                                @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                                                                                @RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        PaginationResponse<IncomeDto, Income> allIncomesByCategory = incomeService.getAllIncomesByCategory(userId, categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(allIncomesByCategory);
    }
    @GetMapping("/{userId}/month/{month}/year/{year}/incomes")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<Set<IncomeDto>> getAllIncomesByMonthAndYear(@PathVariable String userId,
                                                                        @PathVariable int month,
                                                                        @PathVariable int year) {
        Set<IncomeDto> allIncomesByMonthAndYear = this.incomeService.getAllIncomesByMonthAndYear(userId, month, year);
        return new ResponseEntity<>(allIncomesByMonthAndYear, HttpStatus.OK);
    }

    @GetMapping("/{userId}/incomes/{incomeId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<IncomeDto> getSingleIncome(@PathVariable String userId
            , @PathVariable String incomeId) {
        IncomeDto incomeDto = this.incomeService.getIncomeById(userId, incomeId);
        return new ResponseEntity<>(incomeDto, HttpStatus.OK);
    }

    @PutMapping("/{userId}/categories/{categoryId}/incomes/{incomeId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<IncomeDto> updateIncome(@PathVariable String userId,
                                                  @PathVariable String incomeId,
                                                  @PathVariable String categoryId,
                                                  @Valid @RequestBody IncomeDto incomeDto) {
        IncomeDto updateIncome = this.incomeService.updateIncome(userId, incomeId, categoryId, incomeDto);
        return new ResponseEntity<>(updateIncome, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/incomes/{incomeId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ApiResponse> deleteIncome(@PathVariable String userId,
                                                    @PathVariable String incomeId) {
        this.incomeService.deleteIncome(userId, incomeId);
        return new ResponseEntity<>(new ApiResponse("Income with income id " + incomeId + " deleted successfully!", true),
                HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/incomes")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<ApiResponse> deleteAllIncomes(@PathVariable String userId) {
        this.incomeService.deleteAllIncomes(userId);
        return new ResponseEntity<>(new ApiResponse("All incomes of user with user id:" + userId + " deleted successfully!",
                true), HttpStatus.OK);
    }
}
