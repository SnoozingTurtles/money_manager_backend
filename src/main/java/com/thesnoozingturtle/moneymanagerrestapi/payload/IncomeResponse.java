package com.thesnoozingturtle.moneymanagerrestapi.payload;

import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeResponse {
    private List<IncomeDto> incomes;
    private boolean isLastPage;
    private int pageNumber;
    private int numberOfElementsOnSinglePage;
    private int totalPages;
    private long totalElements;
}
