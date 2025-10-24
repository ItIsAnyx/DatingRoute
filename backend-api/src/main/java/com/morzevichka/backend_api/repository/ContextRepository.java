package com.morzevichka.backend_api.repository;

import com.morzevichka.backend_api.entity.Context;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContextRepository extends JpaRepository<Context, Long> {
    Optional<Context> findByChatId(Long chatId);
}
