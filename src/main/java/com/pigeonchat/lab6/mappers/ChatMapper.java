package com.pigeonchat.lab6.mappers;

import com.pigeonchat.lab6.dto.ChatResponseDTO;
import com.pigeonchat.lab6.entity.Chat;

import com.pigeonchat.lab6.utils.MessageMapperHelper;
import com.pigeonchat.lab6.utils.ProfileMapperHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MessageMapperHelper.class, ProfileMapperHelper.class})
public interface ChatMapper {
    @Mapping(source = "messages", target = "messagesId", qualifiedByName = "mapMessageToIds")
    @Mapping(source = "profiles", target = "profilesId", qualifiedByName = "mapProfileToIds")
    ChatResponseDTO toResponseDTO(Chat chat);

    @Mapping(source = "messagesId", target = "messages", qualifiedByName = "mapIdsToMessages")
    @Mapping(source = "profilesId", target = "profiles", qualifiedByName = "mapIdsToProfiles")
    Chat toEntity(ChatResponseDTO responseDTO);
}
