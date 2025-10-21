package com.morzevichka.backend_api.mapper;

import com.morzevichka.backend_api.dto.cache.CachedUser;
import com.morzevichka.backend_api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CachedUserMapper {
    public CachedUser toCache(User user) {
        return new CachedUser(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }

    public User toUser(CachedUser cachedUser) {
        return User.builder()
                .id(cachedUser.id())
                .login(cachedUser.login())
                .email(cachedUser.email())
                .password(cachedUser.password())
                .role(cachedUser.role())
                .chats(null)
                .messages(null)
                .build();
    }
}
