package com.example.KeycloakDemo.controller;


import com.example.KeycloakDemo.request.UserCreateRequest;
import com.example.KeycloakDemo.response.UserResponse;
import com.example.KeycloakDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String saveUser(@RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userName}")
    public UserResponse getUserByUserName(@PathVariable String userName) {
        return userService.getUserByUserName(userName);
    }
}
