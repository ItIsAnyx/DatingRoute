package com.morzevichka.backend_api.domain.repository;

import com.morzevichka.backend_api.domain.model.Context;

public interface ContextRepository {

    Context findByChatId(Long chatId);

    Context save(Context context);
}
