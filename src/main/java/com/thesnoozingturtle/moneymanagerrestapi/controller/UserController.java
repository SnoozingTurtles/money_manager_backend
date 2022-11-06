package com.thesnoozingturtle.moneymanagerrestapi.controller;

import com.thesnoozingturtle.moneymanagerrestapi.dto.UserDto;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import com.thesnoozingturtle.moneymanagerrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String test() {
        return "user controller working fine!";
    }

    @PostMapping("/registerUser")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        System.out.println("User Dto:" + userDto);
        UserDto savedUser = this.userService.registerNewUser(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long userId) {
        UserDto userDto = this.userService.getUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Set<UserDto>> getAllUsers() {
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable int userId,
                                              @RequestBody UserDto userDto) {
        UserDto updatedUser = this.userService.updateUser(userId, userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long userId) {
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(
                new ApiResponse("User with id " + userId + " successfully deleted", true),
                HttpStatus.OK);
    }
}
