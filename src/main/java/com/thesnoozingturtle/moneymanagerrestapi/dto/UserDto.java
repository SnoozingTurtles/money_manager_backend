package com.thesnoozingturtle.moneymanagerrestapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Expense;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Income;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private long balance;
    Set<Expense> expenses = new HashSet<>();
    Set<Income> incomes = new HashSet<>();
}
