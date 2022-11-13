package com.thesnoozingturtle.moneymanagerrestapi.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private String category;
    private String type;
    private long amount;
    private String imageName;
    private LocalDateTime dateAdded;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
