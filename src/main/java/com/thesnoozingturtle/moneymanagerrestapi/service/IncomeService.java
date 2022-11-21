package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Income;
import com.thesnoozingturtle.moneymanagerrestapi.payload.PaginationResponse;

import java.util.Set;

public interface IncomeService {
    IncomeDto addIncome(long userId, IncomeDto incomeDto);
    IncomeDto updateIncome(long userId, long incomeId, IncomeDto incomeDto);
    IncomeDto getIncomeById(long userId, long incomeId);
    PaginationResponse<IncomeDto, Income> getAllIncomes(long userId, int pageNumber, int pageSize, String sortBy, String sortOrder);
    void deleteIncome(long userId, long incomeId);
    void deleteAllIncomes(long userId);
}
