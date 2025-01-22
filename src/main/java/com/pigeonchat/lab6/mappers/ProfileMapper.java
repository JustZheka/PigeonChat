package com.pigeonchat.lab6.mappers;

import com.pigeonchat.lab6.dto.ProfileRequestDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.entity.Profile;
import org.modelmapper.ModelMapper;

public class ProfileMapper extends BaseMapper<ProfileRequestDTO, ProfileResponseDTO, Profile> {
    private final ModelMapper modelMapper = new ModelMapper();

    public Profile toEntity(ProfileResponseDTO responseDTO) {
        return modelMapper.map(responseDTO, Profile.class);
    }
}
