package com.thesnoozingturtle.moneymanagerrestapi.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ApiResponse {
    private final String message;
    private final boolean success;
}
