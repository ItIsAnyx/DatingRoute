package com.morzevichka.backend_api.infrastructure.jpa;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.repository.ChatRepository;
import com.morzevichka.backend_api.domain.repository.ContextRepository;
import com.morzevichka.backend_api.domain.repository.RouteRepository;
import com.morzevichka.backend_api.infrastructure.exception.chat.ChatNotFoundException;
import com.morzevichka.backend_api.infrastructure.jpa.entity.ChatEntity;
import com.morzevichka.backend_api.infrastructure.jpa.mapper.JpaChatMapper;
import com.morzevichka.backend_api.infrastructure.jpa.repository.JpaChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaChatRepositoryAdapter implements ChatRepository {

    private final JpaChatRepository jpa;
    private final RouteRepository routeRepository;
    private final ContextRepository contextRepository;
    private final JpaChatMapper mapper;

    @Override
    public List<Chat> findAllByUserId(Long userId) {
        List<ChatEntity> entities = jpa.findAllByUserId(userId);

        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Chat getReferenceByChatId(Long chatId) {
        ChatEntity entity = jpa.getReferenceById(chatId);
        return Chat.builder().id(entity.getId()).build();
    }

    @Override
    public boolean existsById(Long chatId) {
        return jpa.existsById(chatId);
    }

    @Override
    public Chat save(Chat chat) {
        ChatEntity entity = mapper.toEntity(chat);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public void delete(Chat chat) {
        contextRepository.deleteByChatId(chat.getId());
        routeRepository.deleteByChatId(chat.getId());
        jpa.deleteById(chat.getId());
    }

    @Override
    public Chat findById(Long chatId) {
        return mapper.toDomain(jpa.findById(chatId).orElseThrow(() -> new ChatNotFoundException(chatId)));
    }
}
