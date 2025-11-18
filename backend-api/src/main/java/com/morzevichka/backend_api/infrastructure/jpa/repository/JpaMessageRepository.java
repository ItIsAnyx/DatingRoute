package com.morzevichka.backend_api.infrastructure.jpa.repository;

import com.morzevichka.backend_api.infrastructure.jpa.entity.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMessageRepository extends JpaRepository<MessageEntity, Long> {

    Page<MessageEntity> findAllByChatId(Long chatId, Pageable pageable);
}
