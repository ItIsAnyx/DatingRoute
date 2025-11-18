package com.morzevichka.backend_api.domain.model;

import com.morzevichka.backend_api.domain.value.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String login;
    private String email;
    private String passwordHash;
    private Role role = Role.USER;
}
