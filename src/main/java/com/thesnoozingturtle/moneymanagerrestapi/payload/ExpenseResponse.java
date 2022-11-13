package com.thesnoozingturtle.moneymanagerrestapi.payload;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    private List<ExpenseDto> expenses;
    private boolean isLastPage;
    private int pageNumber;
    private int numberOfElementsOnSinglePage;
    private int totalPages;
    private long totalElements;
}
