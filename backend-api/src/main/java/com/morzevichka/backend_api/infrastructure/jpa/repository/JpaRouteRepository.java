package com.morzevichka.backend_api.infrastructure.jpa.repository;

import com.morzevichka.backend_api.infrastructure.jpa.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaRouteRepository extends JpaRepository<RouteEntity, UUID> {
    Optional<RouteEntity> findByChatId(Long chatId);

    @Query("SELECT r FROM RouteEntity r JOIN FETCH r.routePoints rp WHERE r.chat.id = :chatId")
    Optional<RouteEntity> findByChatIdWithPoints(@Param("chatId") Long chatId);

    @Query("SELECT r FROM RouteEntity r JOIN FETCH r.routePoints rp WHERE r.id = :routeId")
    Optional<RouteEntity> findByIdWithPoints(@Param("routeId") UUID routeId);

    void deleteByChatId(Long chatId);

    boolean existsByChatId(Long chatId);
}
