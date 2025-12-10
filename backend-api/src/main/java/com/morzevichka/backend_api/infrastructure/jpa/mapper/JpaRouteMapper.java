package com.morzevichka.backend_api.infrastructure.jpa.mapper;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.infrastructure.jpa.entity.ChatEntity;
import com.morzevichka.backend_api.infrastructure.jpa.entity.RouteEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaRouteMapper {
    private final JpaChatMapper jpaChatMapper;

    public RouteEntity toEntity(Route route) {
        if (route == null) return null;

        return RouteEntity.builder()
                .id(route.getId())
                .places(route.getPlaces())
                .chat(ChatEntity.builder()
                        .id(route.getChat().getId())
                        .build()
                )
                .updatedAt(route.getUpdatedAt())
                .routeType(route.getType())
                .build();
    }

    public Route toDomain(RouteEntity entity) {
        if (entity == null) return null;

        Route domain = Route.builder()
                .id(entity.getId())
                .places(entity.getPlaces())
                .type(entity.getRouteType())
                .updatedAt(entity.getUpdatedAt())
                .build();

        if (Hibernate.isInitialized(entity.getChat()) && entity.getChat() != null) {
            domain.setChat(jpaChatMapper.toDomain(entity.getChat()));
        } else {
            domain.setChat(Chat.builder().id(entity.getChatId()).build());
        }

        return domain;
    }
}
