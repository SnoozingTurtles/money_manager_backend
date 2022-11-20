package com.thesnoozingturtle.moneymanagerrestapi.repositories;

import com.thesnoozingturtle.moneymanagerrestapi.entity.Expense;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface ExpensesRepo extends JpaRepository<Expense, Long> {
    Expense getExpenseByIdAndUser(long expenseId, User user);
    Page<Expense> getExpensesByUser(User user, Pageable pageable);
    Set<Expense> getExpensesByUser(User user);

    @Query("SELECT e FROM Expense e WHERE MONTH(e.dateAdded) = :month AND " +
            "YEAR(e.dateAdded) = :year AND e.user = :user")
    Set<Expense> getExpensesByMonthAndYearAndUser(@Param(value = "user") User user,
                                            @Param(value = "month") int month,
                                            @Param(value = "year") int year);
    Page<Expense> getExpensesByDateAddedBetweenAndUser(LocalDateTime startDate, LocalDateTime endDate, User user, Pageable pageable);
}
