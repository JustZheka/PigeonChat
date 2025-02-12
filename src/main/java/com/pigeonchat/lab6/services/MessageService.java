package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.MessageRequestDTO;
import com.pigeonchat.lab6.dto.MessageResponseDTO;
import com.pigeonchat.lab6.entity.Message;
import com.pigeonchat.lab6.exceptions.MessageNotFoundException;
import com.pigeonchat.lab6.exceptions.UserNotInChatException;
import com.pigeonchat.lab6.mappers.ChatMapper;
import com.pigeonchat.lab6.mappers.MessageMapper;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.MessageRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class MessageService {
    MessageRepository messageRepository;
    MessageMapper messageMapper;
    ChatMapper chatMapper;
    ProfileMapper profileMapper;
    ChatService chatService;
    ProfileService profileService;

    public MessageResponseDTO getMessageById(@NonNull final UUID messageId) {
        return messageMapper.toResponseDTO(messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException("Сообщение не найдено")));
    }

    public List<MessageResponseDTO> getMessagesByChatId(@NonNull final UUID chatId) {
        return messageRepository.findByChatId(chatId).stream()
            .map(messageMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public MessageResponseDTO createMessage(@NonNull final MessageRequestDTO messageRequestDTO) {
        val chat = chatService.getChatById(messageRequestDTO.getChatId());
        val profile = profileService.getProfileById(messageRequestDTO.getProfileId());

        if (!chat.getProfilesId().contains(profile.getId())) {
            throw new UserNotInChatException(
                "Пользователь %s не является участником чата %s".formatted(profile.getId(), chat.getId())
            );
        }

        val message = Message.builder()
            .text(messageRequestDTO.getText())
            .chat(chatMapper.toEntity(chat))
            .profile(profileMapper.toEntity(profile))
            .build();

        chatService.addMessage(chat.getId(), messageRequestDTO);
        return messageMapper.toResponseDTO(messageRepository.save(message));
    }

    public MessageResponseDTO updateMessage(@NonNull final UUID messageId, @NonNull final MessageRequestDTO messageRequestDTO) {
        val message = messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException("Сообщение не найдено"))
            .toBuilder()
            .text(messageRequestDTO.getText())
            .build();

        return messageMapper.toResponseDTO(messageRepository.save(message));
    }

    public void deleteMessage(@NonNull final UUID chatId, @NonNull final UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new MessageNotFoundException("Сообщение не найдено");
        }

        chatService.deleteMessage(chatId, messageId);
        messageRepository.deleteById(messageId);
    }
}
