package com.morzevichka.backend_api.api.dto.route;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class PointDto {
    private String name;
    List<Double> coords;
}
