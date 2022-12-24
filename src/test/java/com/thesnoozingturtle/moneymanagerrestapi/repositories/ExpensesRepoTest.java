package com.thesnoozingturtle.moneymanagerrestapi.repositories;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Expense;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ExpensesRepoTest {

    @Autowired
    private ExpensesRepo expensesRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    void itShouldGetExpenseByIdAndUser() {
        //given
        String description = "testDescription";
        String category = "testCategory";
        String type = "testType";
        String amount = "1000";
        LocalDateTime dateAdded = LocalDateTime.parse("2022-12-08T12:39:21.239");
        String email = "abc@gmail.com";
        String imageName = "default.png";
        User user = new User(1, "Test", "abc", email, null, null, null);
        User savedUser = this.userRepo.save(user);
        Expense savedExpense = this.expensesRepo.save(new Expense(1, description, category, type, amount, imageName, dateAdded, user));

        //when
        Expense expenseByIdAndUser = this.expensesRepo.getExpenseByIdAndUser(1, user);

        //then
        assertThat(expenseByIdAndUser).isEqualTo(savedExpense);

    }

    @Test
    void getExpensesByUser() {
    }

    @Test
    void testGetExpensesByUser() {
    }

    @Test
    void getExpensesByMonthAndYearAndUser() {
    }

    @Test
    void getExpensesByDateAddedBetweenAndUser() {
    }
}