package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.ChatResponseDTO;
import com.pigeonchat.lab6.dto.MessageRequestDTO;
import com.pigeonchat.lab6.dto.MessageResponseDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.entity.Message;
import com.pigeonchat.lab6.mappers.ChatMapper;
import com.pigeonchat.lab6.mappers.MessageMapper;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.MessageRepository;
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
public class MessageService {
    private MessageRepository messageRepository;
    private MessageMapper messageMapper;
    private ChatMapper chatMapper;
    private ProfileMapper profileMapper;
    private ChatService chatService;
    private ProfileService profileService;

    public MessageResponseDTO getMessageById(final UUID messageId) {
        return messageMapper.toResponseDTO(messageRepository.findById((messageId))
                .orElseThrow(() -> new RuntimeException("Сообщение не найдено")));
    }

    public List<MessageResponseDTO> getMessagesByChatId(final UUID chatId) {
        return messageRepository.findByChatId(chatId).stream()
                .map(messageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public MessageResponseDTO createMessage(final MessageRequestDTO messageRequestDTO) {
        Message message = new Message();
        message.setText(messageRequestDTO.getText());

        ChatResponseDTO chat = chatService.getChatById(messageRequestDTO.getChatId());

        ProfileResponseDTO profile = profileService.getProfileById(messageRequestDTO.getProfileId());

        if (!chat.getProfilesId().contains(profile.getId())) {
            throw new RuntimeException("Этот пользователь не является участником чата!");
        }

        chatService.addMessage(chat.getId(), messageRequestDTO);
        message.setChat(chatMapper.toEntity(chat));
        message.setProfile(profileMapper.toEntity(profile));

        return messageMapper.toResponseDTO(messageRepository.save(message));
    }

    public MessageResponseDTO updateMessage(final UUID messageId, final MessageRequestDTO messageRequestDTO) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Сообщение не найдено"));
        message.setText(messageRequestDTO.getText());

        return messageMapper.toResponseDTO(messageRepository.save(message));
    }

    @Transactional
    public void deleteMessage(final UUID chatId, final UUID messageId) {

        chatService.deleteMessage(chatId, messageId);

        messageRepository.deleteById(messageId);
    }
}
