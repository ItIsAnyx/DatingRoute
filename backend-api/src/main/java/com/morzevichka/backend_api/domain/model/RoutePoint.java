package com.morzevichka.backend_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoutePoint {
    private UUID id;
    private Integer orderIndex;
    private String name;
    private String address;
    private Double lat;
    private Double lon;
}
