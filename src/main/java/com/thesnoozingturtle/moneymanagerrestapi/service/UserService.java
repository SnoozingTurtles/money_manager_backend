package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.UserDto;

import java.util.Set;

public interface UserService {
    UserDto registerNewUser(UserDto userDto);
    UserDto updateUser(long userId, UserDto userDto);
    UserDto getUserById(long userId);
    Set<UserDto> getAllUsers();
    void deleteUser(int userId);
}
