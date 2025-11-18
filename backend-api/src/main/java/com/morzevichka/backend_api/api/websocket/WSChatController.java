package com.morzevichka.backend_api.api.websocket;

import com.morzevichka.backend_api.api.dto.message.MessageRequest;
import com.morzevichka.backend_api.api.dto.message.MessageResponse;
import com.morzevichka.backend_api.application.service.MessageApplicationService;
import com.morzevichka.backend_api.application.usecase.MessageUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WSChatController {

    private final MessageUseCase messageUseCase;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public MessageResponse sendMessage(@Payload @Valid MessageRequest request) {
        return messageUseCase.send(request);
    }
}
