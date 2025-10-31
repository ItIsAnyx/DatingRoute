package com.morzevichka.backend_api.controller;

import com.morzevichka.backend_api.dto.error.DefaultErrorResponse;
import com.morzevichka.backend_api.dto.user.UserInfoResponse;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.mapper.UserMapper;
import com.morzevichka.backend_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(headers = "Authorization", produces = "application/json")
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
    })
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
       return ResponseEntity.ok(userMapper.toDto(users));
    }

    @GetMapping(value = "/me", headers = "Authorization", produces = "application/json")
    @Operation(summary = "Get current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
    })
    public ResponseEntity<UserInfoResponse> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
