package com.thesnoozingturtle.moneymanagerrestapi.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TokenAuthResponse {
    private UUID userId;
    private String accessToken;
    private String refreshToken;
}
