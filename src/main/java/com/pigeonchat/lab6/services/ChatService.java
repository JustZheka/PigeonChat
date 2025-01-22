package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.ChatRequestDTO;
import com.pigeonchat.lab6.dto.ChatResponseDTO;
import com.pigeonchat.lab6.dto.MessageRequestDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.entity.Chat;
import com.pigeonchat.lab6.entity.Message;
import com.pigeonchat.lab6.entity.Profile;
import com.pigeonchat.lab6.mappers.ChatMapper;
import com.pigeonchat.lab6.mappers.MessageMapper;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.ChatRepository;
import com.pigeonchat.lab6.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ProfileRepository profileRepository;


    private final ChatMapper chatMapper = new ChatMapper();
    private final ProfileMapper profileMapper = new ProfileMapper();

    public ChatResponseDTO getChatById(UUID chatId) {
        return chatMapper.toResponseDTO(chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден")), ChatResponseDTO.class);
    }

    public List<ChatResponseDTO> getAllChats() {
        return chatRepository.findAll().stream()
                .map(x -> chatMapper.toResponseDTO(x, ChatResponseDTO.class))
                .collect(Collectors.toList());
    }

    public ChatResponseDTO createChat(ChatRequestDTO chatRequestDTO) {
        Chat chat = new Chat();
        chat.setTitle(chatRequestDTO.getTitle());
        chat.setAvatar(chatRequestDTO.getAvatar());
        return chatMapper.toResponseDTO(chatRepository.save(chat), ChatResponseDTO.class);
    }

    public ChatResponseDTO updateChat(UUID chatId, ChatRequestDTO chatRequestDTO) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
        chat.setTitle(chatRequestDTO.getTitle());
        chat.setAvatar(chatRequestDTO.getAvatar());
        return chatMapper.toResponseDTO(chatRepository.save(chat), ChatResponseDTO.class);
    }

    @Transactional
    public void deleteChat(UUID chatId) {
        chatRepository.deleteById(chatId);
    }

    public ChatResponseDTO addParticipantToChat(UUID chatId, UUID profileId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Профиль не найден"));

        if (chat.getProfiles().contains(profile))
            return chatMapper.toResponseDTO(chat, ChatResponseDTO.class);

        profile.getChats().add(chat);
        profileRepository.save(profile);
        chat.getProfiles().add(profile);
        return chatMapper.toResponseDTO(chatRepository.save(chat), ChatResponseDTO.class);
    }

    public ChatResponseDTO removeParticipantFromChat(UUID chatId, UUID profileId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Профиль не найден"));

        chat.getProfiles().remove(profile);
        return chatMapper.toResponseDTO(chatRepository.save(chat), ChatResponseDTO.class);
    }

    public List<ProfileResponseDTO> getParticipantsByChatId(UUID chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
        return chat.getProfiles().stream()
                .map(x -> profileMapper.toResponseDTO(x, ProfileResponseDTO.class))
                .collect(Collectors.toList());
    }

    public void deleteMessage(UUID chatId, UUID messageId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Чат не найден"));

        chat.getMessages().removeIf(x -> x.getId().equals(messageId));

        chatRepository.save(chat);
    }

    public void addMessage(UUID chatId, MessageRequestDTO message) {
        MessageMapper mapper = new MessageMapper();
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Чат не найден"));
        chat.getMessages().add(mapper.toEntity(message, Message.class));
        chatRepository.save(chat);
    }
}
