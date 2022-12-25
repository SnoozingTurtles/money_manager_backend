package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    RefreshToken createRefreshToken(long userId);
    RefreshToken verifyExpiration(RefreshToken refreshToken);
    int deleteRefreshTokenByUser(long userId);
}
