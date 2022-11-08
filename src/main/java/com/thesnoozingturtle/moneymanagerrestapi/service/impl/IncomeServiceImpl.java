package com.thesnoozingturtle.moneymanagerrestapi.service.impl;

import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Income;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import com.thesnoozingturtle.moneymanagerrestapi.exception.UserNotFoundException;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.IncomeRepo;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.UserRepo;
import com.thesnoozingturtle.moneymanagerrestapi.service.IncomeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IncomeServiceImpl implements IncomeService {

    private final UserRepo userRepo;
    private final IncomeRepo incomeRepo;
    private final ModelMapper modelMapper;

    public IncomeServiceImpl(UserRepo userRepo, IncomeRepo incomeRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.incomeRepo = incomeRepo;
        this.modelMapper = modelMapper;
    }
    @Override
    public IncomeDto addIncome(long userId, IncomeDto incomeDto) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Income income = this.modelMapper.map(incomeDto, Income.class);
        income.setUser(user);
        income.setDateAdded(new Date(System.currentTimeMillis()));
        income.setImageName("Default.png");
        Income savedIncome = this.incomeRepo.save(income);
        return this.modelMapper.map(savedIncome, IncomeDto.class);
    }

    @Override
    public IncomeDto updateIncome(long userId, long incomeId, IncomeDto incomeDto) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Income income = this.incomeRepo.getIncomeByIdAndUser(incomeId, user);
        income.setAmount(incomeDto.getAmount());
        income.setDescription(incomeDto.getDescription());
        income.setType(incomeDto.getType());
        income.setDateAdded(incomeDto.getDateAdded());
        this.incomeRepo.save(income);
        return this.modelMapper.map(income, IncomeDto.class);
    }

    @Override
    public IncomeDto getIncomeById(long userId, long incomeId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Income incomeById = this.incomeRepo.getIncomeByIdAndUser(incomeId, user);
        return this.modelMapper.map(incomeById, IncomeDto.class);
    }

    @Override
    public Set<IncomeDto> getAllIncomes(long userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Set<Income> incomes = this.incomeRepo.getIncomeByUser(user);
        Set<IncomeDto> incomeDtos = incomes.stream()
                .map(income -> this.modelMapper.map(income, IncomeDto.class))
                .collect(Collectors.toSet());
        return incomeDtos;
    }

    @Override
    public void deleteIncome(long userId, long incomeId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("No user exists with the given id!"));
        Income incomeById = this.incomeRepo.getIncomeByIdAndUser(incomeId, user);
        this.incomeRepo.delete(incomeById);
    }
}
