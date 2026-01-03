package com.morzevichka.backend_api.infrastructure.jpa;

import com.morzevichka.backend_api.domain.model.Message;
import com.morzevichka.backend_api.domain.repository.MessageRepository;
import com.morzevichka.backend_api.infrastructure.jpa.entity.MessageEntity;
import com.morzevichka.backend_api.infrastructure.jpa.mapper.JpaMessageMapper;
import com.morzevichka.backend_api.infrastructure.jpa.repository.JpaMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaMessageRepositoryAdapter implements MessageRepository {

    private final JpaMessageRepository jpa;
    private final JpaMessageMapper mapper;

    @Override
    public Page<Message> findAllByChatId(Long id, Pageable pageable) {
        return jpa.findAllByChatId(id, pageable).map(mapper::toDomain);
    }

    @Override
    public Message save(Message message) {
        MessageEntity entity = mapper.toEntity(message);
        return mapper.toDomain(jpa.save(entity));
    }
}
