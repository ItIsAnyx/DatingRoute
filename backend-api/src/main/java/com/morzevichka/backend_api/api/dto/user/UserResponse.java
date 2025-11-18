package com.morzevichka.backend_api.api.dto.user;

import com.morzevichka.backend_api.domain.value.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UserResponse {
    private Long id;
    private String login;
    private String email;
    private Role role;
}
