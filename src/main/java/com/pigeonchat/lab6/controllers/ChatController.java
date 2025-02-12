package com.pigeonchat.lab6.controllers;

import com.pigeonchat.lab6.dto.ChatRequestDTO;
import com.pigeonchat.lab6.dto.ChatResponseDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    final ChatService chatService;

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatResponseDTO> getChat(@PathVariable final UUID chatId) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.getChatById(chatId));
    }

    @GetMapping
    public ResponseEntity<List<ChatResponseDTO>> getAllChats() {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.getAllChats());
    }

    @PostMapping
    public ResponseEntity<ChatResponseDTO> createChat(@RequestBody ChatRequestDTO chatRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createChat(chatRequestDTO));
    }

    @PutMapping("/{chatId}")
    public ResponseEntity<ChatResponseDTO> updateChat(@PathVariable final UUID chatId, @RequestBody ChatRequestDTO chatRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.updateChat(chatId, chatRequestDTO));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable final UUID chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{chatId}/participants/{profileId}")
    public ResponseEntity<ChatResponseDTO> addParticipantToChat(@PathVariable final UUID chatId, @PathVariable final UUID profileId) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.addParticipantToChat(chatId, profileId));
    }

    @DeleteMapping("/{chatId}/participants/{profileId}")
    public ResponseEntity<ChatResponseDTO> removeParticipantFromChat(@PathVariable final UUID chatId, @PathVariable final UUID profileId) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.removeParticipantFromChat(chatId, profileId));
    }

    @GetMapping("/{chatId}/participants")
    public ResponseEntity<List<ProfileResponseDTO>> getParticipantsByChatId(@PathVariable final UUID chatId) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.getParticipantsByChatId(chatId));
    }

}
