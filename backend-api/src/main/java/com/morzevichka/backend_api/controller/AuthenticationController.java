package com.morzevichka.backend_api.controller;

import com.morzevichka.backend_api.dto.authentication.*;
import com.morzevichka.backend_api.dto.error.DefaultErrorResponse;
import com.morzevichka.backend_api.dto.error.ValidationErrorResponse;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.mapper.AuthenticationMapper;
import com.morzevichka.backend_api.security.CustomUserDetails;
import com.morzevichka.backend_api.security.JwtProvider;
import com.morzevichka.backend_api.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationMapper authenticationMapper;
    private final JwtProvider jwtProvider;

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Register user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered"),
            @ApiResponse(responseCode = "400", description = "Invalid body request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))}),
    })
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        User user = authenticationService.register(request);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        return ResponseEntity.ok(
                authenticationMapper.toDto(
                        jwtProvider.generateToken(userDetails),
                        jwtProvider.generateRefreshToken(userDetails)
                )
        );
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Logged"),
            @ApiResponse(responseCode = "400", description = "Invalid body request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))}),
    })
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) {
        User user = authenticationService.login(request);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        return ResponseEntity.ok(
                authenticationMapper.toDto(
                        jwtProvider.generateToken(userDetails),
                        jwtProvider.generateRefreshToken(userDetails)
                )
        );
    }

    @PostMapping(value = "/refresh", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Get a new access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token was given"),
            @ApiResponse(responseCode = "400", description = "Invalid body request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Token Expired",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))})
    })
    public ResponseEntity<RefreshResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        String refreshToken = authenticationService.refresh(request);
        return ResponseEntity.ok(authenticationMapper.toRefreshDto(refreshToken));
    }
}
