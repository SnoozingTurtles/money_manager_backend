package com.thesnoozingturtle.moneymanagerrestapi.controller;

import com.thesnoozingturtle.moneymanagerrestapi.dto.UserDto;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import com.thesnoozingturtle.moneymanagerrestapi.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registerUser")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        System.out.println("User Dto:" + userDto);
        UserDto savedUser = this.userService.registerNewUser(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "Find User by user id",
                notes = "Provide user id to look up the user from the list of users",
                response = UserDto.class)
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@ApiParam(value = "Enter user id") @PathVariable long userId) {
        UserDto userDto = this.userService.getUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<Set<UserDto>> getAllUsers() {
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<UserDto> updateUser(@PathVariable int userId,
                                              @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = this.userService.updateUser(userId, userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long userId) {
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(
                new ApiResponse("User with id " + userId + " successfully deleted", true),
                HttpStatus.OK);
    }
}
