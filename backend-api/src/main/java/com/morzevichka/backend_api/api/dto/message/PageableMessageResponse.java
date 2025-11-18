package com.morzevichka.backend_api.api.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class PageableMessageResponse {
    private List<MessageResponse> messages;
    private int page;
    private int size;
    @JsonProperty("total_pages")
    private int totalPages;
}
