package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Context;
import com.morzevichka.backend_api.entity.ContextJson;
import com.morzevichka.backend_api.entity.ContextRole;
import com.morzevichka.backend_api.repository.ContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContextService {

    private static final Integer maxValuesInJson = 10;
    private final ContextRepository contextRepository;

    @Cacheable(value = "contexts", key = "#context.chatId")
    public Context saveContext(Context context) {
        return contextRepository.save(validContextJson(context));
    }

    public Context saveContext(Chat chat, String userMessage, String aiMessage) {
        return saveContext(createContext(chat, userMessage, aiMessage));
    }

    public Context findContextByChatId(Long chatId) {
        return contextRepository.findByChatId(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Context with chat's id: " + chatId + " not found"));
    }

    public Context validContextJson(Context context) {
        List<ContextJson> contextJsons = context.getContext();
        contextJsons = contextJsons.size() > 10
                ? contextJsons.subList(maxValuesInJson, contextJsons.size())
                : contextJsons;

        context.setContext(contextJsons);

        return context;
    }

    public Context createContext(Chat chat, String userMessage, String aiMessage) {
        List<ContextJson> contextJsons = List.of(
                new ContextJson(ContextRole.USER, userMessage),
                new ContextJson(ContextRole.ASSISTANCE, aiMessage)
        );

        return Context.builder()
                .chat(chat)
                .context(contextJsons)
                .build();
    }
}
