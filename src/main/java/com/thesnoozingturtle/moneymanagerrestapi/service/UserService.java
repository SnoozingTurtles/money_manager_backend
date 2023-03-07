package com.thesnoozingturtle.moneymanagerrestapi.service;

import com.thesnoozingturtle.moneymanagerrestapi.dto.UserDto;

import java.util.Set;

public interface UserService {
    UserDto registerNewUser(UserDto userDto);
    UserDto updateUser(String userId, UserDto userDto);
    UserDto getUserById(String userId);
    Set<UserDto> getAllUsers();
    void deleteUser(String userId);
}
