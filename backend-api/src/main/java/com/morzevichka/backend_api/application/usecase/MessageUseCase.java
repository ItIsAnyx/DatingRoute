package com.morzevichka.backend_api.application.usecase;

import com.morzevichka.backend_api.application.dto.ai.AiCreateClientResponse;
import com.morzevichka.backend_api.application.dto.ai.AiClientResponse;
import com.morzevichka.backend_api.api.dto.commands.SendMessageCommand;
import com.morzevichka.backend_api.api.dto.message.MessageRequest;
import com.morzevichka.backend_api.api.dto.message.MessageResponse;
import com.morzevichka.backend_api.api.dto.message.PageableMessageResponse;
import com.morzevichka.backend_api.application.mapper.MessageMapper;
import com.morzevichka.backend_api.application.service.ChatApplicationService;
import com.morzevichka.backend_api.application.service.ContextApplicationService;
import com.morzevichka.backend_api.application.service.MessageApplicationService;
import com.morzevichka.backend_api.application.service.UserApplicationService;
import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Context;
import com.morzevichka.backend_api.domain.model.Message;
import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.domain.service.ChatService;
import com.morzevichka.backend_api.domain.value.InnerContext;
import com.morzevichka.backend_api.infrastructure.client.AiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageUseCase {

    private final MessageMapper mapper;
    private final MessageApplicationService messageApplicationService;
    private final ChatApplicationService chatApplicationService;
    private final UserApplicationService userApplicationService;
    private final AiClient aiClient;
    private final ContextApplicationService contextApplicationService;
    private final MessageMapper messageMapper;
    private final ChatService chatService;

    public PageableMessageResponse getPageableMessages(Long chatId, Pageable pageable) {
        Page<Message> messagesPage = messageApplicationService.findAllByChatId(chatId, pageable);

        List<MessageResponse> messages = messagesPage.stream()
                .map(mapper::toResponse)
                .toList();

        return PageableMessageResponse.builder()
                .messages(messages)
                .page(messagesPage.getNumber())
                .size(messagesPage.getSize())
                .totalPages(messagesPage.getTotalPages())
                .build();
    }

    @Transactional
    public MessageResponse send(MessageRequest request, Long chatId, Principal principal) {
        User user;
        if (Objects.nonNull(principal)) {
            user = userApplicationService.findByEmail(principal.getName());
        } else {
            user = userApplicationService.getCurrentUser();
        }

        SendMessageCommand cmd = new SendMessageCommand(chatId, user, request.message(), principal);

        if (cmd.chatId() == null) {
            return sendFirstMessage(cmd);
        } else {
            return sendExistingChat(cmd);
        }
    }

    private MessageResponse sendFirstMessage(SendMessageCommand cmd) {
        AiCreateClientResponse ai = aiClient.createChatRequest(cmd.content());
        log.info("Response from AI, title: {}, text: {}, context: {}", ai.getTitle(), ai.getMessage(), ai.getContext());

        chatService.isTitleEmpty(ai.getTitle());

        Chat chat = chatApplicationService.createChat(ai.getTitle(), cmd.user());
        log.info("Chat was created, id: {}, title: {}, userId: {}", chat.getId(), chat.getTitle(), chat.getUser().getId());

        return saveMessagesAndContext(chat, cmd.user(), ai.getContext(), cmd.content(), ai.getMessage());
    }

    private MessageResponse sendExistingChat(SendMessageCommand cmd) {
        Chat chat;
        if (Objects.nonNull(cmd.principal())) {
            chat = chatApplicationService.getChatForCurrentUserWs(cmd.chatId(), cmd.principal());
        } else {
            chat = chatApplicationService.getChatForCurrentUser(cmd.chatId());
        }

        Context context = contextApplicationService.getContext(chat.getId());

        log.info("Context: {}", context.getInnerContexts());

        AiClientResponse ai = aiClient.sendMessageRequest(cmd.content(), context);

        log.info("Response from AI, text: {}, context: {}", ai.getMessage(), ai.getContext());

        return saveMessagesAndContext(chat, cmd.user(), ai.getContext(), cmd.content(), ai.getMessage());
    }

    private MessageResponse saveMessagesAndContext(Chat chat, User user, List<InnerContext> context, String userMessageContent, String aiMessageContent) {
        Message userFuture =
                messageApplicationService.createUserMessage(chat, user, userMessageContent);

        Message aiFuture =
                messageApplicationService.createAiMessage(chat, user, aiMessageContent);

        contextApplicationService.saveContext(chat, context);

        return messageMapper.toResponse(aiFuture);
    }
}
