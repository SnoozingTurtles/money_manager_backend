package com.thesnoozingturtle.moneymanagerrestapi.service.impl;

import com.thesnoozingturtle.moneymanagerrestapi.payload.JwtAuthRequest;
import com.thesnoozingturtle.moneymanagerrestapi.payload.RefreshTokenRequest;
import com.thesnoozingturtle.moneymanagerrestapi.payload.TokenAuthResponse;

public interface AuthService {
    TokenAuthResponse generateToken(JwtAuthRequest request);
    TokenAuthResponse refreshAccessToken(RefreshTokenRequest request);
}
