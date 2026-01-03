package com.morzevichka.backend_api.domain.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RouteType {
    CAR,
    TRANSPORT,
    PEDESTRIAN;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static RouteType fromJson(String value) {
        return RouteType.valueOf(value.toUpperCase());
    }
}
