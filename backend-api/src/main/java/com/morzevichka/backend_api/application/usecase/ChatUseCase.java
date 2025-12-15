package com.morzevichka.backend_api.application.usecase;

import com.morzevichka.backend_api.api.dto.ai.AiSummarizeResponse;
import com.morzevichka.backend_api.api.dto.chat.ChatCreateRequest;
import com.morzevichka.backend_api.api.dto.chat.ChatCreateResponse;
import com.morzevichka.backend_api.api.dto.chat.ChatResponse;
import com.morzevichka.backend_api.api.dto.commands.SendMessageCommand;
import com.morzevichka.backend_api.api.dto.message.MessageResponse;
import com.morzevichka.backend_api.application.mapper.ChatMapper;
import com.morzevichka.backend_api.application.service.ChatApplicationService;
import com.morzevichka.backend_api.application.service.ContextApplicationService;
import com.morzevichka.backend_api.application.service.UserApplicationService;
import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Context;
import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.domain.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatUseCase {

    private final ChatApplicationService chatApplicationService;
    private final ChatMapper chatMapper;
    private final UserApplicationService userApplicationService;
    private final MessageUseCase messageUseCase;
    private final ContextApplicationService contextApplicationService;
    private final ChatService chatService;

    public List<ChatResponse> getUserChats() {

        List<Chat> chats = chatApplicationService.getUserChats();

        return chats.stream()
                .map(chatMapper::toResponse)
                .toList();
    }

    public ChatCreateResponse createChat(ChatCreateRequest request, boolean test) {
        User user = userApplicationService.getCurrentUser();

        MessageResponse messageResponse = messageUseCase.send(new SendMessageCommand(null, user, request.message()), test);

        Chat chat = chatApplicationService.getChat(messageResponse.getChatId());

        return chatMapper.toCreateResponse(chat, messageResponse);
    }

    public AiSummarizeResponse summarize(Long chatId) {
        User user = userApplicationService.getCurrentUser();
        Chat chat = chatApplicationService.getChat(chatId);

        chatService.isUserInChat(user.getId(), chat.getUser().getId());

        Context context = contextApplicationService.getContext(chatId);

        return chatApplicationService.summarize(context);
    }

    public boolean updateChat(ChatResponse request) {
        return false;
    }

    public void deleteChat(Long id) {

    }
}
