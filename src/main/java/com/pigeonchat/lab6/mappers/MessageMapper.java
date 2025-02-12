package com.pigeonchat.lab6.mappers;

import com.pigeonchat.lab6.dto.MessageRequestDTO;
import com.pigeonchat.lab6.dto.MessageResponseDTO;
import com.pigeonchat.lab6.entity.Message;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ChatMapper.class, ProfileMapper.class})
public interface MessageMapper {
    @Mapping(source = "chat.id", target = "chatId")
    @Mapping(source = "profile.id", target = "profileId")
    MessageResponseDTO toResponseDTO(Message message);

    @Mapping(source = "chatId", target = "chat.id")
    @Mapping(source = "profileId", target = "profile.id")
    Message toEntity(MessageRequestDTO messageRequestDTO);
}
