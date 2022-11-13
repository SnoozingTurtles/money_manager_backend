package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import java.util.Set;

public interface IncomeService {
    IncomeDto addIncome(long userId, IncomeDto incomeDto);
    IncomeDto updateIncome(long userId, long incomeId, IncomeDto incomeDto);
    IncomeDto getIncomeById(long userId, long incomeId);
    Set<IncomeDto> getAllIncomes(long userId);
    void deleteIncome(long userId, long incomeId);
}
