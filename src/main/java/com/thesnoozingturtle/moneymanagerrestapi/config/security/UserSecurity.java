package com.thesnoozingturtle.moneymanagerrestapi.config.security;

import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import com.thesnoozingturtle.moneymanagerrestapi.exception.EntityNotFoundException;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {
    private final UserRepo userRepo;

    public UserSecurity(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean hasUserId(Authentication authentication, long userId) {
        User userFromUserId = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("No user found with given user id"));
        String emailOfUserIdUser = userFromUserId.getEmail();
        String emailOfAuthenticatedUser = authentication.getName();
        return emailOfAuthenticatedUser.equals(emailOfUserIdUser);
    }
}
