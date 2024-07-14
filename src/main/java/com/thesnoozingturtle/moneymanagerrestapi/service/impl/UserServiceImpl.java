package com.thesnoozingturtle.moneymanagerrestapi.service.impl;

import com.thesnoozingturtle.moneymanagerrestapi.config.AppConstants;
import com.thesnoozingturtle.moneymanagerrestapi.dto.UserDto;
import com.thesnoozingturtle.moneymanagerrestapi.entity.Role;
import com.thesnoozingturtle.moneymanagerrestapi.entity.User;
import com.thesnoozingturtle.moneymanagerrestapi.exception.EntityNotFoundException;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.RoleRepo;
import com.thesnoozingturtle.moneymanagerrestapi.repositories.UserRepo;
import com.thesnoozingturtle.moneymanagerrestapi.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    @Override
    @Transactional
    public UserDto registerNewUser(UserDto userDto) {
        userDto.setBalance("0");
        User user = this.modelMapper.map(userDto, User.class);
        Role normalUserRole = roleRepo.findById(AppConstants.NORMAL_USER).get();
        user.getRoles().add(normalUserRole);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        User savedUser = this.userRepo.save(user);
        return this.modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto updateUser(String userId, UserDto userDto) {
        User user = getUser(userId);
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        User updatedUser = this.userRepo.save(user);
        return this.modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = getUser(userId);
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public Set<UserDto> getAllUsers() {
        List<User> users = this.userRepo.findAll();
        Set<UserDto> userSet = users.stream()
                .map(user -> this.modelMapper.map(user, UserDto.class))
                .collect(Collectors.toSet());
        return userSet;
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        User user = getUser(userId);
        this.userRepo.delete(user);
    }

    private User getUser(String userId) {
        User user = this.userRepo.findById(UUID.fromString(userId)).orElseThrow(() -> new EntityNotFoundException("No user found with id:" + userId));
        return user;
    }
}
