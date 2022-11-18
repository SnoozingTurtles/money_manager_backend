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

    @Column(length = 150)
    private String description;

    @Column(nullable = false)
    private String category;
    private String type;

    @Column(nullable = false)
    private String amount;
    private String imageName;

    @Column(nullable = false)
    private LocalDateTime dateAdded;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
