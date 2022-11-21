package com.thesnoozingturtle.moneymanagerrestapi.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T, V> {
    private List<T> dtoData;
    private boolean isLastPage;
    private int pageNumber;
    private int numberOfElementsOnSinglePage;
    private int totalPages;
    private long totalElements;
    public PaginationResponse(Page<V> responsePage, List<T> dtoData) {
        this.dtoData = dtoData;
        this.isLastPage = responsePage.isLast();
        this.pageNumber = responsePage.getNumber();
        this.numberOfElementsOnSinglePage = responsePage.getNumberOfElements();
        this.totalPages = responsePage.getTotalPages();
        this.totalElements = responsePage.getTotalElements();
    }
}
