package com.thesnoozingturtle.moneymanagerrestapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class IncomeDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @Size(min = 0, max = 150, message = "Description cannot be more than 150 characters long")
    private String description;
    private String type;

    @Pattern(regexp = "^[1-9]\\d*(\\.\\d+)?$", message = "Please enter a valid amount")
    private String amount;
    @JsonIgnore
    private String imageName;
    @NotEmpty(message = "Date added cannot be empty")
    private String dateAdded;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDto user;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CategoryDto category;
}
