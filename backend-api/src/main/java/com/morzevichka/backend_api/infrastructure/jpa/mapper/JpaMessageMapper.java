package com.morzevichka.backend_api.infrastructure.jpa.mapper;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Message;
import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.infrastructure.jpa.entity.ChatEntity;
import com.morzevichka.backend_api.infrastructure.jpa.entity.MessageEntity;
import com.morzevichka.backend_api.infrastructure.jpa.entity.UserEntity;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
public class JpaMessageMapper {

    private final JpaUserMapper jpaUserMapper;
    private final JpaChatMapper jpaChatMapper;

    public JpaMessageMapper(JpaUserMapper jpaUserMapper, JpaChatMapper jpaChatMapper) {
        this.jpaUserMapper = jpaUserMapper;
        this.jpaChatMapper = jpaChatMapper;
    }

    public MessageEntity toEntity(Message message) {
        if (message == null) return null;

        return MessageEntity.builder()
                .id(message.getId())
                .content(message.getContent())
                .messageType(message.getType())
                .sendDate(message.getSendDate())
                .chat(ChatEntity.builder()
                        .id(message.getChat().getId())
                        .build())
                .user(UserEntity.builder()
                        .id(message.getUser().getId())
                        .build())
                .build();

    }

    public Message toDomain(MessageEntity entity) {
        if (entity == null) return null;

        Message domain = Message.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .type(entity.getMessageType())
                .sendDate(entity.getSendDate())
                .build();

        if (Hibernate.isInitialized(entity.getChat()) && entity.getChat() != null) {
            domain.setChat(jpaChatMapper.toDomain(entity.getChat()));
        } else {
            domain.setChat(Chat.builder().id(entity.getChatId()).build());
        }

        if (Hibernate.isInitialized(entity.getUser()) && entity.getUser() != null) {
            domain.setUser(jpaUserMapper.toDomain(entity.getUser()));
        } else {
            domain.setUser(User.builder().id(entity.getUserId()).build());
        }

        return domain;
    }
}
