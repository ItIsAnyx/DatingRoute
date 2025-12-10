package com.morzevichka.backend_api.infrastructure.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class RouteClient {

    private final RouteClientProperties properties;

    private WebClient webClient;

    @PostConstruct
    private void setWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl(properties.getUrl())
                .build();
    }


}
