package com.morzevichka.backend_api.controller;

import com.morzevichka.backend_api.application.chat.CreateChatUseCase;
import com.morzevichka.backend_api.application.message.SendMessageUseCase;
import com.morzevichka.backend_api.dto.chat.ChatCreateRequest;
import com.morzevichka.backend_api.dto.chat.ChatCreateResponse;
import com.morzevichka.backend_api.dto.chat.ChatInfoResponse;
import com.morzevichka.backend_api.dto.error.DefaultErrorResponse;
import com.morzevichka.backend_api.dto.error.ValidationErrorResponse;
import com.morzevichka.backend_api.dto.message.MessageRequest;
import com.morzevichka.backend_api.dto.message.MessageResponse;
import com.morzevichka.backend_api.dto.message.PageableMessageResponse;
import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.mapper.ChatMapper;
import com.morzevichka.backend_api.mapper.MessageMapper;
import com.morzevichka.backend_api.service.MessageService;
import com.morzevichka.backend_api.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Tag(name = "Chat")
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;
    private final SendMessageUseCase sendMessageUseCase;
    private final CreateChatUseCase createChatUseCase;

    @GetMapping(headers = "Authorization", produces = "application/json")
    @Operation(summary = "Gets user's chats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found chats"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
    })
    public ResponseEntity<List<ChatInfoResponse>> getUserChats() {
        List<Chat> chats = chatService.getUserChats();
        return ResponseEntity.ok(chatMapper.toChatInfoDto(chats));
    }

    @PostMapping(headers = "Authorization", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Creates a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Chat was created"),
            @ApiResponse(responseCode = "400", description = "Invalid body request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
    })
    public ResponseEntity<ChatCreateResponse> createChat(@RequestBody @Valid ChatCreateRequest request) {
        ChatCreateResponse created = createChatUseCase.execute(request);
        URI location = URI.create("/chats/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping(value = "/{id}", headers = "Authorization", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Updates an existing chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat was updated"),
            @ApiResponse(responseCode = "201", description = "Chat was created"),
            @ApiResponse(responseCode = "400", description = "Invalid body request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Chat was not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))})
    })
    public ResponseEntity<ChatInfoResponse> updateChat(@RequestBody @Valid ChatInfoResponse request) {
        boolean created = chatService.updateChat(chatMapper.toEntity(request));

        if (created) {
            URI location = URI.create("/chats/" + request.id());
            return ResponseEntity.created(location).body(request);
        } else {
            return ResponseEntity.ok(request);
        }
    }

    @DeleteMapping(value = "/{id}", headers = "Authorization")
    @Operation(summary = "Deletes a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Chat was deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Chat was not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))})
    })
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/messages", headers = "Authorization", produces = "application/json")
    @Operation(summary = "Get messages by chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found messages"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Chat was not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))})
    })
    public ResponseEntity<PageableMessageResponse> getPageableChatMessages(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "sendDate", direction = Sort.Direction.ASC)
            @ParameterObject Pageable pageable
    ) {
        List<Message> messages = messageService.getMessagesByChatId(id, pageable);
        return ResponseEntity.ok(messageMapper.toPageableDto(pageable, messages));
    }

    @PostMapping("/send")
    @Operation(summary = "send a message (temp)")
    @Deprecated
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request) {
        return ResponseEntity.ok(sendMessageUseCase.execute(request));
    }
}