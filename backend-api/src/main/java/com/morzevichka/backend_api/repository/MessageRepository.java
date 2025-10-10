package com.morzevichka.backend_api.repository;

import com.morzevichka.backend_api.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> getAllByChatId(Long chatId);
}
