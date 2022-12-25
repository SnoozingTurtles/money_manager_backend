package com.thesnoozingturtle.moneymanagerrestapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    private int id;
    private String roleName;
}
