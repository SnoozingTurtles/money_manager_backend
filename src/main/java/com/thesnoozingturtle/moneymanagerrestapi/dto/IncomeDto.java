package com.thesnoozingturtle.moneymanagerrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDto {
    private String description;
    private String type;
    private long amount;
    private Date dateAdded;
}
