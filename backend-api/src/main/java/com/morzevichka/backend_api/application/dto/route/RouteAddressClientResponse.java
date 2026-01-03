package com.morzevichka.backend_api.application.dto.route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class RouteAddressClientResponse {
    private String name;
    private String address;
}
