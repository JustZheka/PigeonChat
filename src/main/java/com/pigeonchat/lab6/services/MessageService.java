package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.ChatResponseDTO;
import com.pigeonchat.lab6.dto.MessageRequestDTO;
import com.pigeonchat.lab6.dto.MessageResponseDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.entity.Chat;
import com.pigeonchat.lab6.entity.Message;
import com.pigeonchat.lab6.entity.Profile;
import com.pigeonchat.lab6.mappers.ChatMapper;
import com.pigeonchat.lab6.mappers.MessageMapper;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.ChatRepository;
import com.pigeonchat.lab6.repository.MessageRepository;
import com.pigeonchat.lab6.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    @Autowired
    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper = new MessageMapper();
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private ChatService chatService;
    @Autowired
    private ProfileService profileService;

    public MessageResponseDTO getMessageById(UUID messageId) {
        return messageMapper.toResponseDTO(messageRepository.findById((messageId))
                .orElseThrow(() -> new RuntimeException("Сообщение не найдено")), MessageResponseDTO.class);
    }

    public List<MessageResponseDTO> getMessagesByChatId(UUID chatId) {
        return messageRepository.findByChatId(chatId).stream()
                .map(x -> messageMapper.toResponseDTO(x, MessageResponseDTO.class))
                .collect(Collectors.toList());
    }

    public MessageResponseDTO createMessage(MessageRequestDTO messageRequestDTO) {
        ChatMapper chatMapper = new ChatMapper();
        ProfileMapper profileMapper = new ProfileMapper();
        Message message = new Message();
        message.setText(messageRequestDTO.getText());

        ChatResponseDTO chat = chatService.getChatById(messageRequestDTO.getChat());

        ProfileResponseDTO profile = profileService.getProfileById(messageRequestDTO.getProfile());

        if (!chat.getProfiles().contains(profile))
            throw new RuntimeException("Этот пользователь не является участником чата!");

        chatService.addMessage(chat.getId(), messageRequestDTO);
        message.setChat(chatMapper.toEntity(chat));
        message.setProfile(profileMapper.toEntity(profile));

        return modelMapper.map(messageRepository.save(message), MessageResponseDTO.class);
    }

    public MessageResponseDTO updateMessage(UUID messageId, MessageRequestDTO messageRequestDTO) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Сообщение не найдено"));
        message.setText(messageRequestDTO.getText());

        return messageMapper.toResponseDTO(messageRepository.save(message), MessageResponseDTO.class);
    }

    @Transactional
    public void deleteMessage(UUID chatId, UUID messageId) {

        chatService.deleteMessage(chatId, messageId);

        messageRepository.deleteById(messageId);
    }
}
