package com.morzevichka.backend_api.domain.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContextType {
    USER,
    ASSISTANT;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static ContextType fromJson(String value) {
        return ContextType.valueOf(value.toUpperCase());
    }
}
