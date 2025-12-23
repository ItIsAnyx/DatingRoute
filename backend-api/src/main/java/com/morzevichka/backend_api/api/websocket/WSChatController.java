package com.morzevichka.backend_api.api.websocket;

import com.morzevichka.backend_api.api.dto.message.MessageRequest;
import com.morzevichka.backend_api.api.dto.message.MessageResponse;
import com.morzevichka.backend_api.application.service.MessageApplicationService;
import com.morzevichka.backend_api.application.usecase.MessageUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WSChatController {

    private final MessageUseCase messageUseCase;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public MessageResponse sendMessage(
            @Payload @Valid MessageRequest request,
            Principal principal
    ) {
        log.info("WebSocket principal: {}", principal.getName());
        return messageUseCase.send(request);
    }
}
