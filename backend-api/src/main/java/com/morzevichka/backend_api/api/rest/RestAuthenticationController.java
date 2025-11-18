package com.morzevichka.backend_api.api.rest;

import com.morzevichka.backend_api.api.dto.authentication.*;
import com.morzevichka.backend_api.api.dto.error.DefaultErrorResponse;
import com.morzevichka.backend_api.api.dto.error.ValidationErrorResponse;
import com.morzevichka.backend_api.application.usecase.AuthenticationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class RestAuthenticationController {

    private final AuthenticationUseCase authenticationUseCase;

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Register user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered"),
            @ApiResponse(responseCode = "400", description = "Invalid body request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))}),
    })
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        AuthenticationResponse response = authenticationUseCase.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Logged"),
            @ApiResponse(responseCode = "400", description = "Invalid body request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))}),
    })
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthenticationResponse response = authenticationUseCase.login(request);
        return ResponseEntity.ok(response);
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
        RefreshResponse response = authenticationUseCase.refresh(request);
        return ResponseEntity.ok(response);
    }
}
