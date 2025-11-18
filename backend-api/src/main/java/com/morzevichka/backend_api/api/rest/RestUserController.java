package com.morzevichka.backend_api.api.rest;

import com.morzevichka.backend_api.api.dto.error.DefaultErrorResponse;
import com.morzevichka.backend_api.api.dto.user.UserResponse;
import com.morzevichka.backend_api.application.usecase.UserUseCase;
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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class RestUserController {

    private final UserUseCase userUseCase;

    @GetMapping(value = "/me", headers = "Authorization", produces = "application/json")
    @Operation(summary = "Get current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
    })
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserResponse response = userUseCase.getCurrentUser();
        return ResponseEntity.ok(response);
    }
}
