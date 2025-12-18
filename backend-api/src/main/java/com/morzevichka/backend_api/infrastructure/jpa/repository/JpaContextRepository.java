package com.morzevichka.backend_api.infrastructure.jpa.repository;

import com.morzevichka.backend_api.infrastructure.jpa.entity.ContextEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaContextRepository extends JpaRepository<ContextEntity, Long> {

    Optional<ContextEntity> findByChatId(Long chatId);

    void deleteByChatId(Long chatId);
}
