package com.morzevichka.backend_api.dto.response;

import com.morzevichka.backend_api.entity.Role;

public record UserInfoResponse(Long id, String login, String email, Role role) {}
