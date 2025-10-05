package com.morzevichka.backend_api.controller;

import com.morzevichka.backend_api.dto.request.ChatCreateRequest;
import com.morzevichka.backend_api.dto.response.ChatCreateResponse;
import com.morzevichka.backend_api.dto.response.ChatInfoResponse;
import com.morzevichka.backend_api.dto.response.MessageResponse;
import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.mapper.ChatMapper;
import com.morzevichka.backend_api.mapper.MessageMapper;
import com.morzevichka.backend_api.service.MessageService;
import com.morzevichka.backend_api.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @GetMapping
    public ResponseEntity<List<ChatInfoResponse>> getUserChats() {
        List<Chat> chats = chatService.getUserChats();
        List<ChatInfoResponse> response = chatMapper.toDto(chats);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    public ResponseEntity<ChatCreateResponse> createChat(@RequestBody @Valid ChatCreateRequest request) {
        ChatCreateResponse response = chatService.createChat(request.firstMessage());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getChatMessages(@PathVariable Long id) {
        List<Message> messages = messageService.getMessagesByChatId(id);

        List<MessageResponse> response = messageMapper.toDto(messages);

        return ResponseEntity.ok(response);
    }
}
