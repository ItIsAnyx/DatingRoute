package com.morzevichka.backend_api.domain.repository;

import com.morzevichka.backend_api.domain.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MessageRepository {

    Page<Message> findAllByChatId(Long id, Pageable pageable);

    Message save(Message message);
}
