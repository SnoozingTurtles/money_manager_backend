package com.thesnoozingturtle.moneymanagerrestapi.payload;


import lombok.Data;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Data
@Setter
public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;
}
