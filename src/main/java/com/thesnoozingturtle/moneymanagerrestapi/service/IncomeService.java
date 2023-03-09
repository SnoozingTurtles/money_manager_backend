package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Income;
import com.thesnoozingturtle.moneymanagerrestapi.payload.PaginationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IncomeService {
    IncomeDto addIncome(String userId, String categoryId, IncomeDto incomeDto);
    IncomeDto updateIncome(String userId, String incomeId, String categoryId, IncomeDto incomeDto);
    IncomeDto getIncomeById(String userId, String incomeId);
    PaginationResponse<IncomeDto, Income> getAllIncomesByCategory(String userId, String categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder);
    PaginationResponse<IncomeDto, Income> getAllIncomes(String userId, int pageNumber, int pageSize, String sortBy, String sortOrder);
    void deleteIncome(String userId, String incomeId);
    void deleteAllIncomes(String userId);
    Set<IncomeDto> getAllIncomesByMonthAndYear(String userId, int month, int year);
    PaginationResponse<IncomeDto, Income> getAllIncomesBetweenAParticularDate(String startDateStr, String endDateStr, String userId, int pageNumber, int pageSize, String sortBy, String sortOrder);
}
