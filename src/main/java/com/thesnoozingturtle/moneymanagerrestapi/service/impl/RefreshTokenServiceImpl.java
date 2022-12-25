package com.thesnoozingturtle.moneymanagerrestapi.service.impl;

import com.thesnoozingturtle.moneymanagerrestapi.entity.RefreshToken;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import com.thesnoozingturtle.moneymanagerrestapi.exception.EntityNotFoundException;
import com.thesnoozingturtle.moneymanagerrestapi.exception.RefreshTokenException;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.RefreshTokenRepo;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.UserRepo;
import com.thesnoozingturtle.moneymanagerrestapi.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${refreshToken.expirationMs}")
    private long refreshTokenExpirationMs;

    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;

    public RefreshTokenServiceImpl(RefreshTokenRepo refreshTokenRepo, UserRepo userRepo) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return refreshTokenRepo.findByRefreshToken(refreshToken);
    }

    @Override
    public RefreshToken createRefreshToken(long userId) {
        RefreshToken refreshToken = new RefreshToken();
        User user = getUser(userId);

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs));

        //Generate a random UUID refresh token
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        //Save to DB
        refreshToken = refreshTokenRepo.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.delete(refreshToken);
            throw new RefreshTokenException(refreshToken.getRefreshToken(), "Refresh token has expired! Please make a new login request");
        }
        return refreshToken;
    }

    @Override
    public int deleteRefreshTokenByUser(long userId) {
        User user = getUser(userId);
        return refreshTokenRepo.deleteByUser(user);
    }

    private User getUser(long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("No user found with given id!"));
    }
}
