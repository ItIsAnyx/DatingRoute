package com.morzevichka.backend_api.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ContextRole {
    USER,
    ASSISTANCE;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
