package com.thesnoozingturtle.moneymanagerrestapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private String category;
    private String type;
    private long amount;
    private String imageName;
    private Date dateAdded;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
