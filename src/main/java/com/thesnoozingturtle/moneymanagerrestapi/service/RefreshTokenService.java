package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    RefreshToken createRefreshToken(String userId);
    RefreshToken verifyExpiration(RefreshToken refreshToken);
    int deleteRefreshTokenByUser(String userId);
}
