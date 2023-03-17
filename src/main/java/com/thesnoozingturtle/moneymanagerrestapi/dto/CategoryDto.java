package com.thesnoozingturtle.moneymanagerrestapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDto user;
}
