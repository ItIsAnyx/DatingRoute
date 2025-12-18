package com.morzevichka.backend_api.infrastructure.jpa;

import com.morzevichka.backend_api.domain.model.Context;
import com.morzevichka.backend_api.domain.repository.ContextRepository;
import com.morzevichka.backend_api.infrastructure.exception.context.ContextNotFoundException;
import com.morzevichka.backend_api.infrastructure.jpa.entity.ContextEntity;
import com.morzevichka.backend_api.infrastructure.jpa.mapper.JpaContextMapper;
import com.morzevichka.backend_api.infrastructure.jpa.repository.JpaContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaContextRepositoryAdapter implements ContextRepository {

    private final JpaContextRepository jpa;
    private final JpaContextMapper mapper;

    @Override
    public Context findByChatId(Long chatId) {
        ContextEntity entity = jpa.findByChatId(chatId)
                .orElseThrow(() -> new ContextNotFoundException(chatId));
        return mapper.toDomain(entity);
    }

    @Override
    public Context save(Context context) {
        ContextEntity entity = mapper.toEntity(context);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public void deleteByChatId(Long chatId) {
        jpa.deleteByChatId(chatId);
    }
}
