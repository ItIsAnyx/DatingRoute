package com.morzevichka.backend_api.application.service;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Context;
import com.morzevichka.backend_api.domain.repository.ContextRepository;
import com.morzevichka.backend_api.domain.service.ContextService;
import com.morzevichka.backend_api.domain.value.InnerContext;
import com.morzevichka.backend_api.infrastructure.exception.ContextNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContextApplicationService {

    private final ContextRepository contextRepository;
    private final ContextService contextService;

    public Context getContext(Long chatId) {
        return contextRepository.findByChatId(chatId);
    }

    public void saveContext(Chat chat, List<InnerContext> innerContext) {
        Context context;

        try {
            context = getContext(chat.getId());
        } catch (ContextNotFoundException e) {
            context = Context.builder().chat(chat).innerContexts(innerContext).build();
        }

        contextRepository.save(context);
    }
}
