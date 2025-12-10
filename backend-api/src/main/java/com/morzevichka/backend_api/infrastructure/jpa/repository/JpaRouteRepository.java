package com.morzevichka.backend_api.infrastructure.jpa.repository;

import com.morzevichka.backend_api.infrastructure.jpa.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRouteRepository extends JpaRepository<RouteEntity, Long> {
    Optional<RouteEntity> findByChatId(Long chatId);

    void deleteByChatId(Long chatId);
}
