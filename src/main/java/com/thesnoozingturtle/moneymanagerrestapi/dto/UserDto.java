package com.thesnoozingturtle.moneymanagerrestapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thesnoozingturtle.moneymanagerrestapi.entity.RefreshToken;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2, max = 30, message = "Name must be between 2 - 30 characters long")
    private String name;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email is not valid")
    @NotEmpty(message = "e-mail cannot be empty")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 3, max = 25, message = "Password must be between 3 - 25 characters long")
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @JsonIgnore
    private String balance;

    @JsonIgnore
    private Set<ExpenseDto> expenses = new HashSet<>();

    @JsonIgnore
    private Set<IncomeDto> incomes = new HashSet<>();

    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    private RefreshToken refreshToken;

    @JsonIgnore
    private List<CategoryDto> categories;

}
