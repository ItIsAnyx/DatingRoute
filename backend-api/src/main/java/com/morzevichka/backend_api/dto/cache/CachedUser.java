package com.morzevichka.backend_api.dto.cache;

import com.morzevichka.backend_api.entity.Role;

public record CachedUser(Long id, String login, String email, String password, Role role) {
}
