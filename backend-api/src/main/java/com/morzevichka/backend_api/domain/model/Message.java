package com.morzevichka.backend_api.domain.model;

import com.morzevichka.backend_api.domain.value.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private Long id;
    private Chat chat;
    private User user;
    private String content;
    private MessageType type;
    private LocalDateTime sendDate;
}
