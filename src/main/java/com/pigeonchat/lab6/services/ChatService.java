package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.ChatRequestDTO;
import com.pigeonchat.lab6.dto.ChatResponseDTO;
import com.pigeonchat.lab6.dto.MessageRequestDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.entity.Chat;
import com.pigeonchat.lab6.exceptions.ChatNotFoundException;
import com.pigeonchat.lab6.exceptions.ProfileNotFoundException;
import com.pigeonchat.lab6.mappers.ChatMapper;
import com.pigeonchat.lab6.mappers.MessageMapper;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.ChatRepository;
import com.pigeonchat.lab6.repository.ProfileRepository;
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
public class ChatService {
    ChatRepository chatRepository;
    ProfileRepository profileRepository;
    ChatMapper chatMapper;
    ProfileMapper profileMapper;
    MessageMapper messageMapper;

    public ChatResponseDTO getChatById(@NonNull final UUID chatId) {
        return chatMapper.toResponseDTO(chatRepository.findById(chatId)
            .orElseThrow(() -> new ChatNotFoundException("Чат не найден")));
    }

    public List<ChatResponseDTO> getAllChats() {
        return chatRepository.findAll().stream()
            .map(chatMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public ChatResponseDTO createChat(@NonNull final ChatRequestDTO chatRequestDTO) {
        val chat = Chat.builder()
            .title(chatRequestDTO.getTitle())
            .avatar(chatRequestDTO.getAvatar())
            .build();
        return chatMapper.toResponseDTO(chatRepository.save(chat));
    }

    public ChatResponseDTO updateChat(@NonNull final UUID chatId, @NonNull final ChatRequestDTO chatRequestDTO) {
        val chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new ChatNotFoundException("Чат не найден"))
            .toBuilder()
            .title(chatRequestDTO.getTitle())
            .avatar(chatRequestDTO.getAvatar())
            .build();
        return chatMapper.toResponseDTO(chatRepository.save(chat));
    }

    public void deleteChat(@NonNull final UUID chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new ChatNotFoundException("Чат не найден");
        }
        chatRepository.deleteById(chatId);
    }

    public ChatResponseDTO addParticipantToChat(@NonNull UUID chatId, @NonNull UUID profileId) {
        val chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new ChatNotFoundException("Чат не найден"));
        val profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new ProfileNotFoundException("Профиль не найден"));

        if (!chat.getProfiles().contains(profile)) {
            profile.getChats().add(chat);
            profileRepository.save(profile);
            chat.getProfiles().add(profile);
            chatRepository.save(chat);
        }
        return chatMapper.toResponseDTO(chat);
    }

    public ChatResponseDTO removeParticipantFromChat(@NonNull final UUID chatId, @NonNull final UUID profileId) {
        val chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new ChatNotFoundException("Чат не найден"));
        val profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new ProfileNotFoundException("Профиль не найден"));

        chat.getProfiles().remove(profile);
        return chatMapper.toResponseDTO(chatRepository.save(chat));
    }

    public List<ProfileResponseDTO> getParticipantsByChatId(@NonNull final UUID chatId) {
        return chatRepository.findById(chatId)
            .orElseThrow(() -> new ChatNotFoundException("Чат не найден"))
            .getProfiles().stream()
            .map(profileMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public void deleteMessage(@NonNull final UUID chatId, @NonNull final UUID messageId) {
        val chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new ChatNotFoundException("Чат не найден"));

        chat.getMessages().removeIf(value -> value.getId().equals(messageId));
        chatRepository.save(chat);
    }

    public void addMessage(@NonNull final UUID chatId, @NonNull final MessageRequestDTO message) {
        val chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new ChatNotFoundException("Чат не найден"));

        val messageEntity = messageMapper.toEntity(message);

        chat.getMessages().add(messageEntity);
        chatRepository.save(chat);
    }
}