package com.morzevichka.backend_api.api.rest;

import com.morzevichka.backend_api.api.dto.chat.ChatCreateRequest;
import com.morzevichka.backend_api.api.dto.chat.ChatCreateResponse;
import com.morzevichka.backend_api.api.dto.chat.ChatResponse;
import com.morzevichka.backend_api.api.dto.chat.ChatUpdateRequest;
import com.morzevichka.backend_api.api.dto.error.DefaultErrorResponse;
import com.morzevichka.backend_api.api.dto.error.ValidationErrorResponse;
import com.morzevichka.backend_api.api.dto.message.MessageRequest;
import com.morzevichka.backend_api.api.dto.message.MessageResponse;
import com.morzevichka.backend_api.api.dto.message.PageableMessageResponse;
import com.morzevichka.backend_api.application.usecase.ChatUseCase;
import com.morzevichka.backend_api.application.usecase.MessageUseCase;
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
@RequestMapping(value = "/api/chats", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "chat")
public class RestChatController {

    private final ChatUseCase chatUseCase;
    private final MessageUseCase messageUseCase;

    @GetMapping(headers = "Authorization")
    @Operation(summary = "Gets user's chats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found chats"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
    })
    public ResponseEntity<List<ChatResponse>> getUserChats() {
        List<ChatResponse> response = chatUseCase.getUserChats();
        return ResponseEntity.ok(response);
    }

    @PostMapping(headers = "Authorization", consumes = "application/json")
    @Operation(summary = "Creates a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Chat was created"),
            @ApiResponse(responseCode = "400", description = "Invalid body request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
    })
    public ResponseEntity<ChatCreateResponse> createChat(@RequestBody @Valid ChatCreateRequest request) {
        ChatCreateResponse response = chatUseCase.createChat(request);
        URI location = URI.create("/chats/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(value = "/{chatId}", headers = "Authorization", consumes = "application/json")
    @Operation(summary = "Updates an existing chat (can't create new chat)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat was updated"),
            @ApiResponse(responseCode = "400", description = "Invalid body request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Chat was not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))})
    })
    public ResponseEntity<ChatResponse> updateChat(@PathVariable Long chatId, @RequestBody @Valid ChatUpdateRequest request) {
        ChatResponse updated = chatUseCase.updateChat(chatId, request);

        return ResponseEntity.ok(updated);
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
        chatUseCase.deleteChat(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{chatId}/messages", headers = "Authorization")
    @Operation(summary = "Get messages by chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found messages"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Chat was not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DefaultErrorResponse.class))})
    })
    public ResponseEntity<PageableMessageResponse> getPageableChatMessages(
            @PathVariable Long chatId,
            @PageableDefault(size = 20, sort = "sendDate", direction = Sort.Direction.ASC)
            @ParameterObject Pageable pageable
    ) {
        PageableMessageResponse response = messageUseCase.getPageableMessages(chatId, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send")
    @Operation(summary = "send a message (temp)")
    @Deprecated
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody @Valid MessageRequest request) {
        MessageResponse response = messageUseCase.send(request);
        return ResponseEntity.ok(response);
    }
}