package com.thesnoozingturtle.moneymanagerrestapi.controller;

import com.thesnoozingturtle.moneymanagerrestapi.config.security.CustomUserDetails;
import com.thesnoozingturtle.moneymanagerrestapi.config.security.jwtutil.JwtTokenHelper;
import com.thesnoozingturtle.moneymanagerrestapi.entity.RefreshToken;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import com.thesnoozingturtle.moneymanagerrestapi.exception.LoginException;
import com.thesnoozingturtle.moneymanagerrestapi.exception.RefreshTokenException;
import com.thesnoozingturtle.moneymanagerrestapi.payload.JwtAuthRequest;
import com.thesnoozingturtle.moneymanagerrestapi.payload.RefreshTokenRequest;
import com.thesnoozingturtle.moneymanagerrestapi.payload.TokenAuthResponse;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.UserRepo;
import com.thesnoozingturtle.moneymanagerrestapi.service.RefreshTokenService;
import com.thesnoozingturtle.moneymanagerrestapi.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<TokenAuthResponse> generateToken(@RequestBody JwtAuthRequest jwtAuthRequest) {
        TokenAuthResponse tokenAuthResponse = authService.generateToken(jwtAuthRequest);
        return new ResponseEntity<>(tokenAuthResponse, HttpStatus.OK);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenAuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenAuthResponse tokenAuthResponse = authService.refreshAccessToken(refreshTokenRequest);
        return new ResponseEntity<>(tokenAuthResponse, HttpStatus.OK);
    }
}
