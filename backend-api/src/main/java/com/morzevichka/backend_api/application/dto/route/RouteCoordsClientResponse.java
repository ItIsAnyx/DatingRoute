package com.morzevichka.backend_api.application.dto.route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class RouteCoordsClientResponse {
    private String name;
    private String address;
    private List<Double> coords;
}
