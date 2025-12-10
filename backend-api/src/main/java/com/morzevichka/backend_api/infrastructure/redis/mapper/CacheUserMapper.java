package com.morzevichka.backend_api.infrastructure.redis.mapper;

import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.infrastructure.redis.cachedto.UserCache;
import org.springframework.stereotype.Component;

@Component
public class CacheUserMapper {

    public UserCache toCache(User user) {
        return UserCache.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole())
                .build();
    }

    public User toDomain(UserCache cache) {
        return User.builder()
                .id(cache.getId())
                .login(cache.getLogin())
                .email(cache.getEmail())
                .passwordHash(cache.getPasswordHash())
                .role(cache.getRole())
                .build();
    }
}
