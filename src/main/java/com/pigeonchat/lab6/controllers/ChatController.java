package com.pigeonchat.lab6.controllers;

import com.pigeonchat.lab6.dto.ChatRequestDTO;
import com.pigeonchat.lab6.dto.ChatResponseDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatResponseDTO> getChat(@PathVariable UUID chatId) {
        return ResponseEntity.ok(chatService.getChatById(chatId));
    }

    @GetMapping
    public ResponseEntity<List<ChatResponseDTO>> getAllChats() {
        return ResponseEntity.ok(chatService.getAllChats());
    }

    @PostMapping
    public ResponseEntity<ChatResponseDTO> createChat(@RequestBody ChatRequestDTO chatRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createChat(chatRequestDTO));
    }

    @PutMapping("/{chatId}")
    public ResponseEntity<ChatResponseDTO> updateChat(@PathVariable UUID chatId, @RequestBody ChatRequestDTO chatRequestDTO) {
        return ResponseEntity.ok(chatService.updateChat(chatId, chatRequestDTO));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable UUID chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{chatId}/participants/{profileId}")
    public ResponseEntity<ChatResponseDTO> addParticipantToChat(
            @PathVariable UUID chatId,
            @PathVariable UUID profileId
    ) {
        return ResponseEntity.ok(chatService.addParticipantToChat(chatId, profileId));
    }

    @DeleteMapping("/{chatId}/participants/{profileId}")
    public ResponseEntity<ChatResponseDTO> removeParticipantFromChat(
            @PathVariable UUID chatId,
            @PathVariable UUID profileId
    ) {
        return ResponseEntity.ok(chatService.removeParticipantFromChat(chatId, profileId));
    }

    @GetMapping("/{chatId}/participants")
    public ResponseEntity<List<ProfileResponseDTO>> getParticipantsByChatId(@PathVariable UUID chatId) {
        return ResponseEntity.ok(chatService.getParticipantsByChatId(chatId));
    }

}
