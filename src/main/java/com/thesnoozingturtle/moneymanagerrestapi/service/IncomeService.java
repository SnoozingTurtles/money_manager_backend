package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Income;
import com.thesnoozingturtle.moneymanagerrestapi.payload.PaginationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IncomeService {
    IncomeDto addIncome(long userId, IncomeDto incomeDto, MultipartFile image);
    IncomeDto updateIncome(long userId, long incomeId, IncomeDto incomeDto, MultipartFile image);
    IncomeDto getIncomeById(long userId, long incomeId);
    PaginationResponse<IncomeDto, Income> getAllIncomes(long userId, int pageNumber, int pageSize, String sortBy, String sortOrder);
    void deleteIncome(long userId, long incomeId);
    void deleteAllIncomes(long userId);
    Set<IncomeDto> getAllIncomesByMonthAndYear(long userId, int month, int year);
    PaginationResponse<IncomeDto, Income> getAllIncomesBetweenAParticularDate(String startDateStr, String endDateStr, long userId, int pageNumber, int pageSize, String sortBy, String sortOrder);
}
