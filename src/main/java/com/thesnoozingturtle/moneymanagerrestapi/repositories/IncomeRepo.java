package com.thesnoozingturtle.moneymanagerrestapi.repositories;

import com.thesnoozingturtle.moneymanagerrestapi.entity.Income;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IncomeRepo extends JpaRepository<Income, Long> {
    Income getIncomeByIdAndUser(long incomeId, User user);
    Set<Income> getIncomeByUser(User user);
}
