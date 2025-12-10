package com.morzevichka.backend_api.application.usecase;

import com.morzevichka.backend_api.api.dto.route.RoutePlacesRequest;
import com.morzevichka.backend_api.api.dto.route.RoutePlacesResponse;
import com.morzevichka.backend_api.application.service.ChatApplicationService;
import com.morzevichka.backend_api.application.service.ContextApplicationService;
import com.morzevichka.backend_api.application.service.RouteApplicationService;
import com.morzevichka.backend_api.application.service.UserApplicationService;
import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Context;
import com.morzevichka.backend_api.domain.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RouteUseCase {

    private final UserApplicationService userApplicationService;
    private final ChatApplicationService chatApplicationService;
    private final ChatService chatService;
    private final ContextApplicationService contextApplicationService;
    private final RouteApplicationService routeApplicationService;

    public RoutePlacesResponse getPlaces(Long chatId) {
        Chat chat = chatApplicationService.getChatForCurrentUser(chatId);

        Context context = contextApplicationService.getContext(chatId);

        return routeApplicationService.getPlaces(context);
    }

    public RoutePlacesResponse updatePlaces(Long chatId, RoutePlacesRequest placesToUpdate) {
        return null;
    }

    public void buildRoute() {}
}
