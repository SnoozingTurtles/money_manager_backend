package com.thesnoozingturtle.moneymanagerrestapi.repositories;

import com.thesnoozingturtle.moneymanagerrestapi.entity.RefreshToken;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByUser(User user);

    int deleteByUser(User user);
}
