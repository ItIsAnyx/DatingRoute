package com.morzevichka.backend_api.controller;

import com.morzevichka.backend_api.dto.message.MessageRequest;
import com.morzevichka.backend_api.dto.message.MessageResponse;
import com.morzevichka.backend_api.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MessageService messageService;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public MessageResponse sendMessage(@Payload @Valid MessageRequest request) {
        return messageService.sendMessage(request);
    }
}
