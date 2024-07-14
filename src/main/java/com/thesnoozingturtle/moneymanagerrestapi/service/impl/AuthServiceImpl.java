package com.thesnoozingturtle.moneymanagerrestapi.service.impl;

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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final JwtTokenHelper jwtTokenHelper;
    private final RefreshTokenService refreshTokenService;
    @Override
    public TokenAuthResponse generateToken(JwtAuthRequest jwtAuthRequest) {
        this.authenticate(jwtAuthRequest.getEmail(), jwtAuthRequest.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getEmail());
        String jwtToken = this.jwtTokenHelper.generateToken(userDetails);

        //Fetching user to send user id in response
        User user = this.userRepo.findByEmail(jwtAuthRequest.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId().toString());
        return new TokenAuthResponse(user.getId(), jwtToken, refreshToken.getRefreshToken());
    }

    @Override
    public TokenAuthResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        return refreshTokenService.findByRefreshToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtTokenHelper.generateToken(new CustomUserDetails(user));
                    return new TokenAuthResponse(user.getId(), accessToken, refreshToken);
                }).orElseThrow(() -> new RefreshTokenException(refreshToken, "No such refresh token found!"));
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
}
