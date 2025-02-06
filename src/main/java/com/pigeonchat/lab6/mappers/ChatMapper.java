package com.pigeonchat.lab6.mappers;

import com.pigeonchat.lab6.dto.ChatRequestDTO;
import com.pigeonchat.lab6.dto.ChatResponseDTO;
import com.pigeonchat.lab6.entity.Chat;

import com.pigeonchat.lab6.entity.Message;
import com.pigeonchat.lab6.entity.Profile;
import com.pigeonchat.lab6.repository.MessageRepository;
import com.pigeonchat.lab6.repository.ProfileRepository;
import com.pigeonchat.lab6.utils.MessageMapperHelper;
import com.pigeonchat.lab6.utils.ProfileMapperHelper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {MessageMapperHelper.class, ProfileMapperHelper.class})
public interface ChatMapper {
    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    @Mapping(source = "messages", target = "messagesId", qualifiedByName = "mapMessageToIds")
    @Mapping(source = "profiles", target = "profilesId", qualifiedByName = "mapProfileToIds")
    ChatResponseDTO toResponseDTO(Chat chat);

    @Mapping(source = "messagesId", target = "messages", qualifiedByName = "mapIdsToMessages")
    @Mapping(source = "profilesId", target = "profiles", qualifiedByName = "mapIdsToProfiles")
    Chat toEntity(ChatResponseDTO responseDTO);
}
