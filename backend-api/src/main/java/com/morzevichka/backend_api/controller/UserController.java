package com.morzevichka.backend_api.controller;

import com.morzevichka.backend_api.dto.user.UserInfoResponse;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.mapper.UserMapper;
import com.morzevichka.backend_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
       return ResponseEntity.ok(userMapper.toDto(users));
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
