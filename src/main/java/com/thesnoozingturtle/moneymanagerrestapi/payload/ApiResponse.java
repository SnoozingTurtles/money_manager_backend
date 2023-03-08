package com.thesnoozingturtle.moneymanagerrestapi.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse {
    private final String message;
    private final boolean success;
}
