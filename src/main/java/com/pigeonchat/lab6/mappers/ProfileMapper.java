package com.pigeonchat.lab6.mappers;

import com.pigeonchat.lab6.dto.ProfileRequestDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.entity.Profile;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = ChatMapper.class)
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    ProfileResponseDTO toResponseDTO(Profile profile);

    Profile toEntity(ProfileRequestDTO profileRequestDTO);

    Profile toEntity(ProfileResponseDTO responseDTO);
}
