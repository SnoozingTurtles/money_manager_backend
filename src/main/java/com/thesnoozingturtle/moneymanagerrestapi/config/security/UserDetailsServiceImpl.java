package com.thesnoozingturtle.moneymanagerrestapi.config.security;

import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userByEmail = this.userRepo.findByEmail(email);
        if(userByEmail == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found!");
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(userByEmail);
        return customUserDetails;
    }
}
