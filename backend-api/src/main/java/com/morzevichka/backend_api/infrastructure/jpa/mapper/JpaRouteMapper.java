package com.morzevichka.backend_api.infrastructure.jpa.mapper;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.domain.model.RoutePoint;
import com.morzevichka.backend_api.infrastructure.jpa.entity.ChatEntity;
import com.morzevichka.backend_api.infrastructure.jpa.entity.RouteEntity;
import com.morzevichka.backend_api.infrastructure.jpa.entity.RoutePointEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaRouteMapper {
    private final JpaChatMapper jpaChatMapper;
    private final JpaRoutePointMapper jpaRoutePointMapper;

    public RouteEntity toEntity(Route route) {
        if (route == null) return null;

        RouteEntity entity = new RouteEntity();
        entity.setId(route.getId());
        entity.setChat(ChatEntity.builder().id(route.getChat().getId()).build());
        entity.setRouteType(route.getRouteType());

        List<RoutePointEntity> points = route.getPoints().stream()
                .map(p -> {
                    RoutePointEntity e = jpaRoutePointMapper.toEntity(p);
                    e.setRoute(entity);
                    return e;
                })
                .toList();

        entity.setRoutePoints(points);
        return entity;
    }

    public Route toDomain(RouteEntity entity) {
        if (entity == null) return null;

        Route domain = Route.builder()
                .id(entity.getId())
                .routeType(entity.getRouteType())
                .updatedAt(entity.getUpdatedAt())
                .points(new ArrayList<>())
                .build();

        if (Hibernate.isInitialized(entity.getChat()) && entity.getChat() != null) {
            domain.setChat(jpaChatMapper.toDomain(entity.getChat()));
        } else {
            domain.setChat(Chat.builder().id(entity.getChatId()).build());
        }

        if (Hibernate.isInitialized(entity.getRoutePoints()) && entity.getRoutePoints() != null) {
            entity.getRoutePoints().forEach(p -> {
                RoutePoint point = jpaRoutePointMapper.toDomain(p);
                domain.getPoints().add(point);
            });
        } else {
            domain.setPoints(new ArrayList<>());
        }

        return domain;
    }
}
