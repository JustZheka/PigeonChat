package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.ProfileRequestDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.exceptions.ProfileNotFoundException;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.ProfileRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;

    public List<ProfileResponseDTO> getAllProfiles() {
        return profileRepository.findAll().stream()
            .map(profileMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public ProfileResponseDTO getProfileById(@NonNull final UUID id) {
        val profile = profileRepository.findById(id)
            .orElseThrow(() -> new ProfileNotFoundException("Профиль не найден"));
        return profileMapper.toResponseDTO(profile);
    }

    public List<ProfileResponseDTO> getProfileByUsername(@NonNull final String username) {
        return profileRepository.findByUsername(username).stream()
            .map(profileMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public ProfileResponseDTO createProfile(@NonNull final ProfileRequestDTO profileDTO) {
        val profile = profileMapper.toEntity(profileDTO);
        return profileMapper.toResponseDTO(profileRepository.save(profile));
    }

    public ProfileResponseDTO updateProfile(@NonNull final UUID id, @NonNull final ProfileRequestDTO profileDTO) {
        val existingProfile = profileRepository.findById(id)
            .orElseThrow(() -> new ProfileNotFoundException("Профиль не найден"))
            .toBuilder()
            .username(profileDTO.getUsername())
            .registrationDate(profileDTO.getRegistrationDate())
            .description(profileDTO.getDescription())
            .avatar(profileDTO.getAvatar())
            .email(profileDTO.getEmail())
            .build();

        return profileMapper.toResponseDTO(profileRepository.save(existingProfile));
    }

    public void deleteProfile(@NonNull final UUID id) {
        if (!profileRepository.existsById(id)) {
            throw new ProfileNotFoundException("Профиль не найден");
        }
        profileRepository.deleteById(id);
    }
}