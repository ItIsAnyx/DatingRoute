package com.morzevichka.backend_api.dto.message;

import java.util.List;

public record PageableMessageResponse(
        List<MessageResponse> messages,
        int page,
        int size
) {}
