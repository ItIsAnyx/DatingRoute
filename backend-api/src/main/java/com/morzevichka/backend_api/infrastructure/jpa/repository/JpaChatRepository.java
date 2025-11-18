package com.morzevichka.backend_api.infrastructure.jpa.repository;

import com.morzevichka.backend_api.infrastructure.jpa.entity.ChatEntity;
import com.morzevichka.backend_api.infrastructure.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaChatRepository extends JpaRepository<ChatEntity, Long> {

    List<ChatEntity> findAllByUserId(Long userId);

}
