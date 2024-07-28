package com.thesnoozingturtle.moneymanagerrestapi.controller;

import com.thesnoozingturtle.moneymanagerrestapi.payload.JwtAuthRequest;
import com.thesnoozingturtle.moneymanagerrestapi.payload.RefreshTokenRequest;
import com.thesnoozingturtle.moneymanagerrestapi.payload.TokenAuthResponse;
import com.thesnoozingturtle.moneymanagerrestapi.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
