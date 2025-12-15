package com.morzevichka.backend_api.application.usecase;

import com.morzevichka.backend_api.api.dto.ai.AiCreateResponse;
import com.morzevichka.backend_api.api.dto.ai.AiResponse;
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

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    public MessageResponse send(MessageRequest request, boolean test) {
        User user = userApplicationService.getCurrentUser();
        return send(new SendMessageCommand(request.chatId(), user, request.message()), test);
    }

    public MessageResponse send(SendMessageCommand cmd, boolean test) {
        if (cmd.chatId() == null) {
            return sendFirstMessage(cmd, test);
        } else {
            return sendExistingChat(cmd, test);
        }
    }

    private MessageResponse sendFirstMessage(SendMessageCommand cmd, boolean test) {
        AiCreateResponse ai = aiClient.createChatRequest(cmd.content(), test);
        log.info("Response from AI, title: {}, text: {}, context: {}", ai.getTitle(), ai.getMessage(), ai.getContext());

        chatService.isTitleEmpty(ai.getTitle());

        Chat chat = chatApplicationService.createChat(ai.getTitle(), cmd.user());
        log.info("Chat was created, id: {}, title: {}, userId: {}", chat.getId(), chat.getTitle(), chat.getUser().getId());

        return saveMessagesAndContext(chat, cmd.user(), ai.getContext(), cmd.content(), ai.getMessage());
    }

    private MessageResponse sendExistingChat(SendMessageCommand cmd, boolean test) {
        Chat chat = chatApplicationService.getChat(cmd.chatId());

        chatService.isUserInChat(cmd.user().getId(), chat.getUser().getId());

        Context context = contextApplicationService.getContext(chat.getId());

        log.info("Context: {}", context.getInnerContexts());

        AiResponse ai = aiClient.sendMessageRequest(cmd.content(), context, test);
        log.info("Response from AI, text: {}, context: {}", ai.getMessage(), ai.getContext());

        return saveMessagesAndContext(chat, cmd.user(), ai.getContext(), cmd.content(), ai.getMessage());
    }

    private MessageResponse saveMessagesAndContext(Chat chat, User user, List<InnerContext> context, String userMessageContent, String aiMessageContent) {
        CompletableFuture<Message> userFuture =
                messageApplicationService.createUserMessageAsync(chat, user, userMessageContent);

        CompletableFuture<Message> aiFuture =
                messageApplicationService.createAiMessageAsync(chat, user, aiMessageContent);

        Message userMsg = userFuture.join();
        Message aiMsg = aiFuture.join();

        contextApplicationService.saveContext(chat, context);

        return messageMapper.toResponse(aiMsg);
    }
}
