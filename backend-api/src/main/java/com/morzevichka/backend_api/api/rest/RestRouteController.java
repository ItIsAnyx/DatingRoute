package com.morzevichka.backend_api.api.rest;

import com.morzevichka.backend_api.api.dto.error.DefaultErrorResponse;
import com.morzevichka.backend_api.api.dto.route.RoutePlacesRequest;
import com.morzevichka.backend_api.api.dto.route.RoutePlacesResponse;
import com.morzevichka.backend_api.application.usecase.RouteUseCase;
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
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@Tag(name = "route")
public class RestRouteController {

    private final RouteUseCase routeUseCase;

    @GetMapping(value = "/{chatId}/places", headers = "Authorization")
    @Operation(summary = "Get points from chat history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of places"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Chat was not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))})
    })
    public ResponseEntity<RoutePlacesResponse> places(@PathVariable Long chatId) {
        RoutePlacesResponse response = routeUseCase.getPlaces(chatId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{chatId}/places")
    @Operation(summary = "Add new points into route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Places were added"),
            @ApiResponse(responseCode = "400", description = "Invalid body request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Chat was not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))})
    })
    public ResponseEntity<RoutePlacesResponse> updatePlaces(@PathVariable Long chatId, @RequestBody @Valid RoutePlacesRequest request) {
        RoutePlacesResponse response = routeUseCase.updatePlaces(chatId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{chatId}/build")
    @Operation(summary = "Build route and return suitable json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route was created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Chat was not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))})
    })
    public ResponseEntity<Void> buildRoute(@PathVariable Long chatId) {
        return null;
    }
}

