package com.morzevichka.backend_api.infrastructure.client;

import com.morzevichka.backend_api.api.dto.route.RouteMapKeyResponse;
import com.morzevichka.backend_api.application.dto.route.RouteAddressClientRequest;
import com.morzevichka.backend_api.application.dto.route.RouteAddressClientResponse;
import com.morzevichka.backend_api.application.dto.route.RouteCoordsClientRequest;
import com.morzevichka.backend_api.application.dto.route.RouteCoordsClientResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class RouteClient {

    private final RouteClientProperties properties;

    private WebClient webClient;

    @PostConstruct
    void setWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl(properties.getUrl())
                .build();
    }

    public RouteAddressClientResponse getAddress(String text) {
        RouteAddressClientRequest request = new RouteAddressClientRequest(text);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/maps/addresses")
                        .build()
                )
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RouteAddressClientResponse>() {})
                .block();
    }

    public RouteCoordsClientResponse getCoords(String text, String address) {
        RouteCoordsClientRequest request = new RouteCoordsClientRequest(text, address);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/maps/coords")
                        .build()
                )
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RouteCoordsClientResponse>() {})
                .block();
    }

    public RouteMapKeyResponse getMapKey() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/maps-key")
                        .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RouteMapKeyResponse>() {})
                .block();
    }
}
