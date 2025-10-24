package com.morzevichka.backend_api.controller;

import com.morzevichka.backend_api.dto.chat.ChatCreateRequest;
import com.morzevichka.backend_api.dto.chat.ChatCreateResponse;
import com.morzevichka.backend_api.dto.chat.ChatInfoResponse;
import com.morzevichka.backend_api.dto.message.MessageRequest;
import com.morzevichka.backend_api.dto.message.MessageResponse;
import com.morzevichka.backend_api.dto.message.PageableMessageResponse;
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

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<ChatInfoResponse>> getUserChats() {
        return ResponseEntity.ok(chatService.getUserChats());
    }

    @PostMapping("/start")
    public ResponseEntity<ChatCreateResponse> createChat(@RequestBody @Valid ChatCreateRequest request) {
        return ResponseEntity.ok(chatService.createChat(request));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<PageableMessageResponse> getPageableChatMessages(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "sendDate", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(messageService.getMessagesByChatId(id, pageable));
    }

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(request));
    }
}