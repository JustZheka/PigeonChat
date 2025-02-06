package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.ChatRequestDTO;
import com.pigeonchat.lab6.dto.ChatResponseDTO;
import com.pigeonchat.lab6.dto.MessageRequestDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.entity.Chat;
import com.pigeonchat.lab6.entity.Profile;
import com.pigeonchat.lab6.mappers.ChatMapper;
import com.pigeonchat.lab6.mappers.MessageMapper;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.ChatRepository;
import com.pigeonchat.lab6.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ChatService {
    private ChatRepository chatRepository;
    private ProfileRepository profileRepository;
    private ChatMapper chatMapper;
    private ProfileMapper profileMapper;
    private MessageMapper messageMapper;

    public ChatResponseDTO getChatById(final UUID chatId) {
        return chatMapper.toResponseDTO(chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден")));
    }

    public List<ChatResponseDTO> getAllChats() {
        return chatRepository.findAll().stream()
                .map(chatMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ChatResponseDTO createChat(final ChatRequestDTO chatRequestDTO) {
        Chat chat = new Chat();
        chat.setTitle(chatRequestDTO.getTitle());
        chat.setAvatar(chatRequestDTO.getAvatar());
        return chatMapper.toResponseDTO(chatRepository.save(chat));
    }

    public ChatResponseDTO updateChat(final UUID chatId, final ChatRequestDTO chatRequestDTO) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
        chat.setTitle(chatRequestDTO.getTitle());
        chat.setAvatar(chatRequestDTO.getAvatar());
        return chatMapper.toResponseDTO(chatRepository.save(chat));
    }

    @Transactional
    public void deleteChat(final UUID chatId) {
        chatRepository.deleteById(chatId);
    }

    public ChatResponseDTO addParticipantToChat(UUID chatId, UUID profileId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Профиль не найден"));

        if (chat.getProfiles().contains(profile)) {
            return chatMapper.toResponseDTO(chat);
        }

        profile.getChats().add(chat);
        profileRepository.save(profile);
        chat.getProfiles().add(profile);
        return chatMapper.toResponseDTO(chatRepository.save(chat));
    }

    public ChatResponseDTO removeParticipantFromChat(final UUID chatId, final UUID profileId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Профиль не найден"));
        chat.getProfiles().remove(profile);
        return chatMapper.toResponseDTO(chatRepository.save(chat));
    }

    public List<ProfileResponseDTO> getParticipantsByChatId(final UUID chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
        return chat.getProfiles().stream()
                .map(profileMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteMessage(final UUID chatId, final UUID messageId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Чат не найден"));
        chat.getMessages().removeIf(x -> x.getId().equals(messageId));
        chatRepository.save(chat);
    }

    public void addMessage(final UUID chatId, final MessageRequestDTO message) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Чат не найден"));
        chat.getMessages().add(messageMapper.toEntity(message));
        chatRepository.save(chat);
    }
}
