package com.morzevichka.backend_api.api.dto.route;

import jakarta.validation.constraints.Size;

import java.util.List;

public record RoutePlacesRequest(
        @Size(min = 2, message = "должно быть хотя бы два места")
        List<String> places
) {}
