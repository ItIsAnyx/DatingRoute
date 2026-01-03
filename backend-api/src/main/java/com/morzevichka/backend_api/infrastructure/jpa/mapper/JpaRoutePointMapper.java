package com.morzevichka.backend_api.infrastructure.jpa.mapper;

import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.domain.model.RoutePoint;
import com.morzevichka.backend_api.infrastructure.jpa.entity.RouteEntity;
import com.morzevichka.backend_api.infrastructure.jpa.entity.RoutePointEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaRoutePointMapper {

    public RoutePointEntity toEntity(RoutePoint routePoint) {
        if (routePoint == null) return null;

        return RoutePointEntity.builder()
                .id(routePoint.getId())
                .orderIndex(routePoint.getOrderIndex())
                .name(routePoint.getName())
                .build();
    }

    public RoutePoint toDomain(RoutePointEntity entity) {
        if (entity == null) return null;

        return RoutePoint.builder()
                .id(entity.getId())
                .orderIndex(entity.getOrderIndex())
                .name(entity.getName())
                .build();
    }
}
