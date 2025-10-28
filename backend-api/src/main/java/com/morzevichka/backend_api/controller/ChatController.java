package com.morzevichka.backend_api.controller;

import com.morzevichka.backend_api.application.chat.CreateChatUseCase;
import com.morzevichka.backend_api.application.message.SendMessageUseCase;
import com.morzevichka.backend_api.dto.chat.ChatCreateRequest;
import com.morzevichka.backend_api.dto.chat.ChatCreateResponse;
import com.morzevichka.backend_api.dto.chat.ChatInfoResponse;
import com.morzevichka.backend_api.dto.message.MessageRequest;
import com.morzevichka.backend_api.dto.message.MessageResponse;
import com.morzevichka.backend_api.dto.message.PageableMessageResponse;
import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.mapper.ChatMapper;
import com.morzevichka.backend_api.mapper.MessageMapper;
import com.morzevichka.backend_api.service.MessageService;
import com.morzevichka.backend_api.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;
    private final SendMessageUseCase sendMessageUseCase;
    private final CreateChatUseCase createChatUseCase;

    @GetMapping
    public ResponseEntity<List<ChatInfoResponse>> getUserChats() {
        List<Chat> chats = chatService.getUserChats();
        return ResponseEntity.ok(chatMapper.toChatInfoDto(chats));
    }

    @PostMapping("/start")
    public ResponseEntity<ChatCreateResponse> createChat(@RequestBody @Valid ChatCreateRequest request) {
        return ResponseEntity.ok(createChatUseCase.execute(request));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<PageableMessageResponse> getPageableChatMessages(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "sendDate", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        List<Message> messages = messageService.getMessagesByChatId(id, pageable);
        return ResponseEntity.ok(messageMapper.toPageableDto(pageable, messages));
    }

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request) {
        return ResponseEntity.ok(sendMessageUseCase.execute(request));
    }
}