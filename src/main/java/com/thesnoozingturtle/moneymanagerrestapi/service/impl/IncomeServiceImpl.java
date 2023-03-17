package com.thesnoozingturtle.moneymanagerrestapi.service.impl;

import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Category;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Income;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import com.thesnoozingturtle.moneymanagerrestapi.exception.EntityNotFoundException;
import com.thesnoozingturtle.moneymanagerrestapi.payload.PaginationResponse;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.CategoryRepo;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.IncomeRepo;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.UserRepo;
import com.thesnoozingturtle.moneymanagerrestapi.service.ImageService;
import com.thesnoozingturtle.moneymanagerrestapi.service.IncomeService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IncomeServiceImpl implements IncomeService {

    private final UserRepo userRepo;
    private final IncomeRepo incomeRepo;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final CategoryRepo categoryRepo;
    public IncomeServiceImpl(UserRepo userRepo, IncomeRepo incomeRepo, ModelMapper modelMapper, ImageService imageService, CategoryRepo categoryRepo) {
        this.userRepo = userRepo;
        this.incomeRepo = incomeRepo;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.categoryRepo = categoryRepo;
    }
    @Override
    public IncomeDto addIncome(String userId, String categoryId, IncomeDto incomeDto) {
        User user = getUser(userId);
        Category category = getCategory(categoryId, user);
        Income income = this.modelMapper.map(incomeDto, Income.class);
        user.setBalance(String.valueOf(Double.parseDouble(user.getBalance()) + Double.parseDouble(income.getAmount())));
        income.setUser(user);
        income.setCategory(category);
        LocalDateTime ldt = LocalDateTime.parse(incomeDto.getDateAdded());
        income.setDateAdded(ldt);
        Income savedIncome = this.incomeRepo.save(income);
        this.userRepo.save(user);
        return this.modelMapper.map(savedIncome, IncomeDto.class);
    }

    @Override
    public IncomeDto updateIncome(String userId, String incomeId, String categoryId, IncomeDto incomeDto) {
        User user = getUser(userId);
        Category category = getCategory(categoryId, user);
        Income income = getIncome(incomeId, user);

        //to update the balance in user table
        double prevIncomeAmount = Double.parseDouble(income.getAmount());
        double prevBalance = Double.parseDouble(user.getBalance());
        double newBalance = prevBalance - prevIncomeAmount + Double.parseDouble(incomeDto.getAmount());
        user.setBalance(String.valueOf(newBalance));
        this.userRepo.save(user);

        income.setCategory(category);
        income.setAmount(incomeDto.getAmount());
        income.setDescription(incomeDto.getDescription());
        income.setType(incomeDto.getType());
        income.setImageName(incomeDto.getImageName());
        LocalDateTime ldt = LocalDateTime.parse(incomeDto.getDateAdded());
        income.setDateAdded(ldt);

        this.incomeRepo.save(income);
        return this.modelMapper.map(income, IncomeDto.class);
    }

    @Override
    public IncomeDto getIncomeById(String userId, String incomeId) {
        User user = getUser(userId);
        Income incomeById = getIncome(incomeId, user);
        return this.modelMapper.map(incomeById, IncomeDto.class);
    }

    @Override
    public PaginationResponse<IncomeDto, Income> getAllIncomesByCategory(String userId, String categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        User user = getUser(userId);
        Category category = getCategory(categoryId, user);
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Income> incomeByUserAndCategory = incomeRepo.getIncomeByUserAndCategory(user, category, pageable);
        PaginationResponse<IncomeDto, Income> paginationResponse = getPaginationResponse(incomeByUserAndCategory);
        return paginationResponse;
    }

    @Override
    public PaginationResponse<IncomeDto, Income> getAllIncomes(String userId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        User user = getUser(userId);
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Income> incomesPage = this.incomeRepo.getIncomeByUser(user, pageable);
        PaginationResponse<IncomeDto, Income> paginationResponse = getPaginationResponse(incomesPage);
        return paginationResponse;
    }

    @Override
    public void deleteIncome(String userId, String incomeId) {
        User user = getUser(userId);
        Income incomeById = getIncome(incomeId, user);
        this.incomeRepo.delete(incomeById);
    }

    @Override
    public void deleteAllIncomes(String userId) {
        User user = getUser(userId);
        Set<Income> incomes = this.incomeRepo.getIncomeByUser(user);
        this.incomeRepo.deleteAll(incomes);
    }

    //Method to get incomes by providing a particular month and year

    @Override
    public Set<IncomeDto> getAllIncomesByMonthAndYear(String userId, int month, int year) {
        User user = getUser(userId);
        Set<Income> incomes = this.incomeRepo.getIncomesByMonthAndYearAndUser(user, month, year);
        Set<IncomeDto> incomeDtos = incomes.stream()
                .map(income -> this.modelMapper.map(income, IncomeDto.class))
                .collect(Collectors.toSet());
        return incomeDtos;
    }
    //Method to get incomes by providing a particular month and year

    @Override
    public PaginationResponse<IncomeDto, Income> getAllIncomesBetweenAParticularDate(String startDateStr, String endDateStr, String userId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        User user = getUser(userId);
        LocalDateTime startDate = LocalDateTime.parse(startDateStr);
        LocalDateTime endDate;
        if(endDateStr != null) {
            endDate = LocalDateTime.parse(endDateStr);
        } else {
            endDate = LocalDateTime.now();
        }
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Income> incomesPage = this.incomeRepo.getIncomesByDateAddedBetweenAndUser(startDate, endDate, user, pageable);
        PaginationResponse<IncomeDto, Income> paginationResponse = getPaginationResponse(incomesPage);
        return paginationResponse;
    }
    //Get the user from userId

    private User getUser(String userId) {
        User user = this.userRepo.findById(UUID.fromString(userId)).orElseThrow(() -> new EntityNotFoundException("No user exists with the given id!"));
        return user;
    }

    //get the category from categoryId
    private Category getCategory(String categoryId, User user) {
        Category category = categoryRepo.findCategoryByIdAndUser(UUID.fromString(categoryId), user)
                .orElseThrow(() -> new EntityNotFoundException("No such category found!"));
        return category;
    }

    //Get the Pageable object from parameters

    private Pageable getPageable(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = (sortOrder.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return pageable;
    }
    //Generate an object of PaginationResponse

    private PaginationResponse<IncomeDto, Income> getPaginationResponse(Page<Income> incomesPage) {
        List<Income> incomesByUser = incomesPage.getContent();
        List<IncomeDto> allIncomes = incomesByUser
                .stream()
                .map(income -> this.modelMapper.map(income, IncomeDto.class))
                .collect(Collectors.toList());

        PaginationResponse<IncomeDto, Income> paginationResponse = new PaginationResponse<>(incomesPage, allIncomes);
        return paginationResponse;
    }
    private Income getIncome(String incomeId, User user) {
        Income income = this.incomeRepo.getIncomeByIdAndUser(UUID.fromString(incomeId), user)
                .orElseThrow(() -> new EntityNotFoundException("No such income found!"));
        return income;
    }
}
