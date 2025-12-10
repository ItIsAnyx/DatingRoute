package com.morzevichka.backend_api.infrastructure.redis.mapper;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.infrastructure.redis.cachedto.RouteCache;
import org.springframework.stereotype.Component;

@Component
public class CacheRouteMapper {

    public RouteCache toCache(Route route) {
        return RouteCache.builder()
                .id(route.getId())
                .chatId(route.getChat().getId())
                .places(route.getPlaces())
                .updatedAt(route.getUpdatedAt())
                .type(route.getType())
                .build();
    }

    public Route toDomain(RouteCache cache) {
        return Route.builder()
                .id(cache.getId())
                .chat(Chat.builder().id(cache.getChatId()).build())
                .places(cache.getPlaces())
                .updatedAt(cache.getUpdatedAt())
                .type(cache.getType())
                .build();
    }
}
