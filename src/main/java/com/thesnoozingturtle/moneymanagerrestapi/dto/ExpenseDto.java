package com.thesnoozingturtle.moneymanagerrestapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @Size(min = 0, max = 150, message = "Description cannot be more than 150 characters long")
    private String description;
    private String type;

    @Pattern(regexp = "^[1-9]\\d*(\\.\\d+)?$", message = "Please enter a valid amount")
    private String amount;

    @NotEmpty(message = "Date added cannot be empty")
    private String dateAdded;
}
