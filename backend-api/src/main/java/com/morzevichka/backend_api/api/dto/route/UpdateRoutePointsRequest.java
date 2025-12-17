package com.morzevichka.backend_api.api.dto.route;

import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateRoutePointsRequest(
        @Size(min = 2, message = "Маршрут должен содержать более одного места")
        List<String> points
) {
}
