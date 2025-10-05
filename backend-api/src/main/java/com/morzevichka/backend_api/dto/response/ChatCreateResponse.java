package com.morzevichka.backend_api.dto.response;

import com.morzevichka.backend_api.entity.Message;

public record ChatCreateResponse(Long id, String title, MessageResponse response) {
}
