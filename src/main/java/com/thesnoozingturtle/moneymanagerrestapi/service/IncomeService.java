package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import java.util.Set;

public interface IncomeService {
    IncomeDto addIncome(IncomeDto incomeDto);
    IncomeDto updateIncome(long incomeId, IncomeDto incomeDto);
    IncomeDto getIncomeById(long incomeId);
    Set<IncomeDto> getAllIncomes();
    void deleteIncome(int incomeId);
}
