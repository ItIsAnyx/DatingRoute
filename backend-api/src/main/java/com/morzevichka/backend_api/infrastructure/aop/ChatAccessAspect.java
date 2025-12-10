package com.morzevichka.backend_api.infrastructure.aop;

import com.morzevichka.backend_api.application.service.UserApplicationService;
import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.domain.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ChatAccessAspect {

    private final UserApplicationService userApplicationService;
    private final ChatService chatService;

    @Before("@annotation(ValidateUserInChat) && args(chatId,..)")
    public void checkAccess(Long chatId) {
        User user = userApplicationService.getCurrentUser();
        chatService.isUserInChat(user.getId(), chatId);
    }
}
