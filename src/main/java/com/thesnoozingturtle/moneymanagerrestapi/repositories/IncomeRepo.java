package com.thesnoozingturtle.moneymanagerrestapi.repositories;

import com.thesnoozingturtle.moneymanagerrestapi.entity.Income;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface IncomeRepo extends JpaRepository<Income, Long> {
    Optional<Income> getIncomeByIdAndUser(UUID incomeId, User user);
    Page<Income> getIncomeByUser(User user, Pageable pageable);
    Set<Income> getIncomeByUser(User user);
    @Query("SELECT i FROM Income i WHERE MONTH(i.dateAdded) = :month AND " +
            "YEAR(i.dateAdded) = :year AND i.user = :user")
    Set<Income> getIncomesByMonthAndYearAndUser(@Param(value = "user") User user,
                                                  @Param(value = "month") int month,
                                                  @Param(value = "year") int year);
    Page<Income> getIncomesByDateAddedBetweenAndUser(LocalDateTime startDate, LocalDateTime endDate, User user, Pageable pageable);
}
