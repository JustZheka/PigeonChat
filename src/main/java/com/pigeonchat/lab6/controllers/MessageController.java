package com.pigeonchat.lab6.controllers;

import com.pigeonchat.lab6.dto.MessageRequestDTO;
import com.pigeonchat.lab6.dto.MessageResponseDTO;
import com.pigeonchat.lab6.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponseDTO> getMessage(@PathVariable UUID messageId) {
        return ResponseEntity.ok(messageService.getMessageById(messageId));
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesByChatId(@PathVariable UUID chatId) {
        return ResponseEntity.ok(messageService.getMessagesByChatId(chatId));
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> createMessage(@RequestBody MessageRequestDTO messageRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.createMessage(messageRequestDTO));
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<MessageResponseDTO> updateMessage(
            @PathVariable UUID messageId,
            @RequestBody MessageRequestDTO messageRequestDTO
    ) {
        return ResponseEntity.ok(messageService.updateMessage(messageId, messageRequestDTO));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId, @RequestParam UUID chatId) {
        messageService.deleteMessage(chatId, messageId);
        return ResponseEntity.noContent().build();
    }
}