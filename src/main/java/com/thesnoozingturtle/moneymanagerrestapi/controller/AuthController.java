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
public class AuthController {
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final JwtTokenHelper jwtTokenHelper;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, UserRepo userRepo, JwtTokenHelper jwtTokenHelper, RefreshTokenService refreshTokenService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.jwtTokenHelper = jwtTokenHelper;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenAuthResponse> generateToken(@RequestBody JwtAuthRequest jwtAuthRequest) {
        this.authenticate(jwtAuthRequest.getEmail(), jwtAuthRequest.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getEmail());
        String jwtToken = this.jwtTokenHelper.generateToken(userDetails);

        //Fetching user to send user id in response
        User user = this.userRepo.findByEmail(jwtAuthRequest.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId().toString());
        TokenAuthResponse tokenAuthResponse = new TokenAuthResponse(user.getId(), jwtToken, refreshToken.getRefreshToken());
        return new ResponseEntity<>(tokenAuthResponse, HttpStatus.OK);
    }

    private void authenticate(String email, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(email, password);
        try {
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException e) {
            throw new LoginException("Invalid email or password");
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenAuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        return refreshTokenService.findByRefreshToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtTokenHelper.generateToken(new CustomUserDetails(user));
                    return new ResponseEntity<>(new TokenAuthResponse(user.getId(), accessToken, refreshToken), HttpStatus.OK);
                }).orElseThrow(() -> new RefreshTokenException(refreshToken, "No such refresh token found!"));
    }
}
