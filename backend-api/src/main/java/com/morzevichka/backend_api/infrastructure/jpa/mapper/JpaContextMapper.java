package com.morzevichka.backend_api.infrastructure.jpa.mapper;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Context;
import com.morzevichka.backend_api.infrastructure.jpa.entity.ChatEntity;
import com.morzevichka.backend_api.infrastructure.jpa.entity.ContextEntity;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
public class JpaContextMapper {

    private final JpaChatMapper jpaChatMapper;

    public JpaContextMapper(JpaChatMapper jpaChatMapper) {
        this.jpaChatMapper = jpaChatMapper;
    }

    public ContextEntity toEntity(Context context) {
        if (context == null) return null;

        return ContextEntity.builder()
                .id(context.getId())
                .chat(ChatEntity.builder()
                        .id(context.getChat().getId())
                        .build())
                .context(context.getInnerContexts())
                .build();
    }

    public Context toDomain(ContextEntity entity) {
        if (entity == null) return null;

        Context domain = Context.builder()
                .id(entity.getId())
                .innerContexts(entity.getContext())
                .build();

        if (Hibernate.isInitialized(entity.getChat()) && entity.getChat() != null) {
            domain.setChat(jpaChatMapper.toDomain(entity.getChat()));
        } else {
            domain.setChat(Chat.builder().id(entity.getChatId()).build());
        }

        return domain;
    }
}
