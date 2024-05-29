package com.mmdevelopement.crm.domain.user.controller;


import com.mmdevelopement.crm.domain.user.entity.dto.UserDto;
import com.mmdevelopement.crm.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public UserDto getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_SUPER_ADMIN"})
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PutMapping("/")
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @PutMapping("/password")
    public void updateUserPassword(@RequestParam("password") String password) {
        userService.updateUserPassword(password);
    }

    @GetMapping("/all")
    @Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
