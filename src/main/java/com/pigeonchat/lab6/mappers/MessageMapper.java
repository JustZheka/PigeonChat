package com.pigeonchat.lab6.mappers;

import com.pigeonchat.lab6.dto.MessageRequestDTO;
import com.pigeonchat.lab6.dto.MessageResponseDTO;
import com.pigeonchat.lab6.entity.Chat;
import com.pigeonchat.lab6.entity.Message;

import com.pigeonchat.lab6.entity.Profile;
import com.pigeonchat.lab6.repository.ChatRepository;
import com.pigeonchat.lab6.repository.ProfileRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {ChatMapper.class, ProfileMapper.class})
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "chat.id", target = "chatId")
    @Mapping(source = "profile.id", target = "profileId")
    MessageResponseDTO toResponseDTO(Message message);

    @Mapping(source = "chatId", target = "chat.id")
    @Mapping(source = "profileId", target = "profile.id")
    Message toEntity(MessageRequestDTO messageRequestDTO);

    @Named("mapChat")
    default Chat mapChat(UUID chatId, @Context ChatRepository chatRepository) {
        return chatId == null ? null : chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
    }

    @Named("mapProfile")
    default Profile mapProfile(UUID profileId, @Context ProfileRepository profileRepository) {
        return profileId == null ? null : profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Профиль не найден"));
    }
}
