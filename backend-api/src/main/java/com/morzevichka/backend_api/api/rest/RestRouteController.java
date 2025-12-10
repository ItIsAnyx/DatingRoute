package com.morzevichka.backend_api.api.rest;

import com.morzevichka.backend_api.api.dto.route.*;
import com.morzevichka.backend_api.application.usecase.RouteUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@Tag(name = "route")
public class RestRouteController {

    private final RouteUseCase routeUseCase;

    @GetMapping
    public ResponseEntity<RouteResponse> getRoute(@RequestParam Long chatId) {
        RouteResponse response = routeUseCase.getRoute(chatId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<RoutePointsResponse> create(@RequestParam Long chatId) {
        RoutePointsResponse response = routeUseCase.create(chatId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{routeId}/places")
    public ResponseEntity<RoutePointsResponse> getPoints(@PathVariable UUID routeId) {
        RoutePointsResponse response = routeUseCase.getRoute(routeId);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{routeId}/places")
    public ResponseEntity<RoutePointsResponse> updateRoute(@PathVariable UUID routeId, @RequestBody @Valid UpdateRoutePointsRequest points) {
        RoutePointsResponse response = routeUseCase.updatePlaces(routeId, points.points());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{routeId}/build")
    public ResponseEntity<RouteBuildResponse> buildRoute(@PathVariable UUID routeId) {
        RouteBuildResponse response = routeUseCase.build(routeId);
        return ResponseEntity.ok(response);
    }
}

