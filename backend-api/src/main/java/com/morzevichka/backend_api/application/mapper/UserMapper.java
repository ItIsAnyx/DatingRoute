package com.morzevichka.backend_api.application.mapper;

import com.morzevichka.backend_api.api.dto.user.UserResponse;
import com.morzevichka.backend_api.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
