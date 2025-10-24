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
                user.getUserRole()
        );
    }

    public User toUser(CachedUser cachedUser) {
        return User.builder()
                .id(cachedUser.id())
                .login(cachedUser.login())
                .email(cachedUser.email())
                .password(cachedUser.password())
                .userRole(cachedUser.userRole())
                .chats(null)
                .messages(null)
                .build();
    }
}
