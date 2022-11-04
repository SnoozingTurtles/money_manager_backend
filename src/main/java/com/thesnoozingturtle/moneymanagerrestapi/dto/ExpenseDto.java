package com.thesnoozingturtle.moneymanagerrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {
    private String description;
    private String category;
    private String type;
    private long amount;
    private Date dateAdded;
}
