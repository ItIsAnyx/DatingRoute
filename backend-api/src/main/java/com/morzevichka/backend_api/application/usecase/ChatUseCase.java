package com.morzevichka.backend_api.application.usecase;

import com.morzevichka.backend_api.api.dto.chat.ChatCreateRequest;
import com.morzevichka.backend_api.api.dto.chat.ChatCreateResponse;
import com.morzevichka.backend_api.api.dto.chat.ChatResponse;
import com.morzevichka.backend_api.api.dto.chat.ChatUpdateRequest;
import com.morzevichka.backend_api.api.dto.commands.SendMessageCommand;
import com.morzevichka.backend_api.api.dto.message.MessageRequest;
import com.morzevichka.backend_api.api.dto.message.MessageResponse;
import com.morzevichka.backend_api.application.mapper.ChatMapper;
import com.morzevichka.backend_api.application.service.ChatApplicationService;
import com.morzevichka.backend_api.application.service.ContextApplicationService;
import com.morzevichka.backend_api.application.service.UserApplicationService;
import com.morzevichka.backend_api.domain.model.Chat;
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

    public List<ChatResponse> getUserChats() {

        List<Chat> chats = chatApplicationService.getUserChats();

        return chats.stream()
                .map(chatMapper::toResponse)
                .toList();
    }

    public ChatCreateResponse createChat(ChatCreateRequest request) {
        MessageResponse messageResponse = messageUseCase.send(new MessageRequest(request.message()), null, null);

        Chat chat = chatApplicationService.getChatForCurrentUser(messageResponse.getChatId());

        return chatMapper.toCreateResponse(chat, messageResponse);
    }


    public ChatResponse updateChat(Long chatId, ChatUpdateRequest request) {
        Chat chat = chatApplicationService.getChatForCurrentUser(chatId);

        chat.setTitle(request.title());

        return chatMapper.toResponse(chatApplicationService.updateChat(chat));
    }

    public void deleteChat(Long chatId) {
        Chat chat = chatApplicationService.getChatForCurrentUser(chatId);

        chatApplicationService.deleteChat(chat);
    }
}
