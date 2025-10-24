package com.morzevichka.backend_api.dto.cache;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morzevichka.backend_api.entity.UserRole;

public record CachedUser(
        Long id,
        String login,
        String email,
        String password,
        UserRole userRole) {
}
