package com.thesnoozingturtle.moneymanagerrestapi.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenAuthResponse {
    private long userId;
    private String accessToken;
    private String refreshToken;
}
