package com.thesnoozingturtle.moneymanagerrestapi.payload;

import lombok.Data;

@Data
public class ApiResponse {
    private final String message;
    private final boolean success;
}
