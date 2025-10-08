package com.morzevichka.backend_api.dto.user;

import com.morzevichka.backend_api.entity.Role;

public record UserInfoResponse(Long id, String login, String email, Role role) {}
