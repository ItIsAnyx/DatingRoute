package com.morzevichka.backend_api.infrastructure.jpa.mapper;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.infrastructure.jpa.entity.ChatEntity;
import com.morzevichka.backend_api.infrastructure.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatJpaMapper {

    private final UserJpaMapper userJpaMapper;

    public ChatEntity toEntity(Chat chat) {
        if (chat == null) return null;

        return ChatEntity.builder()
                .id(chat.getId())
                .title(chat.getTitle())
                .user(UserEntity.builder()
                        .id(chat.getUser().getId())
                        .build())
                .build();
    }

    public Chat toDomain(ChatEntity entity) {
        if (entity == null) return null;

        Chat domain = Chat.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .build();

        if (Hibernate.isInitialized(entity.getUser()) && entity.getUser() != null) {
            domain.setUser(userJpaMapper.toDomain(entity.getUser()));
        } else {
            domain.setUser(User.builder().id(entity.getUserId()).build());
        }

        return domain;
    }
}
