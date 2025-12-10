package com.morzevichka.backend_api.domain.repository;

import com.morzevichka.backend_api.domain.model.Chat;

import java.util.List;

public interface ChatRepository {

    List<Chat> findAllByUserId(Long userId);

    Chat getReferenceByChatId(Long chatId);

    boolean existsById(Long chatId);

    Chat save(Chat chat);

    void delete(Chat chat);

    Chat findById(Long chatId);
}
