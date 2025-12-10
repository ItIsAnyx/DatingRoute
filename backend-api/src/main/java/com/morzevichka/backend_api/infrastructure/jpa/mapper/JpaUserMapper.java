package com.morzevichka.backend_api.infrastructure.jpa.mapper;

import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.infrastructure.jpa.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class JpaUserMapper {

    public UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .password(user.getPasswordHash())
                .role(user.getRole())
                .build();
    }

    public User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .email(entity.getEmail())
                .passwordHash(entity.getPassword())
                .role(entity.getRole())
                .build();
    }
}
