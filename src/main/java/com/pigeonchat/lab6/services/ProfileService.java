package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.ProfileRequestDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.entity.Profile;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.ProfileRepository;
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
public class ProfileService {
    private ProfileRepository profileRepository;
    private ProfileMapper profileMapper;

    public List<ProfileResponseDTO> getAllProfiles() {
        return profileRepository.findAll().stream()
                .map(profileMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProfileResponseDTO getProfileById(final UUID id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Профиль не найден"));
        return profileMapper.toResponseDTO(profile);
    }

    public List<ProfileResponseDTO> getProfileByUsername(final String username) {
        List<Profile> profiles = profileRepository.findByUsername(username);
        return profiles.stream()
                .map(profileMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProfileResponseDTO createProfile(final ProfileRequestDTO profileDTO) {
        Profile profile = profileMapper.toEntity(profileDTO);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toResponseDTO(savedProfile);
    }

    public ProfileResponseDTO updateProfile(final UUID id, final ProfileRequestDTO profileDTO) {
        Profile existingProfile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Профиль не найден"));
        existingProfile.setUsername(profileDTO.getUsername());
        existingProfile.setRegistrationDate(profileDTO.getRegistrationDate());
        existingProfile.setDescription(profileDTO.getDescription());
        existingProfile.setAvatar(profileDTO.getAvatar());
        existingProfile.setEmail(profileDTO.getEmail());
        Profile updatedProfile = profileRepository.save(existingProfile);
        return profileMapper.toResponseDTO(updatedProfile);
    }

    @Transactional
    public void deleteProfile(final UUID id) {
        profileRepository.deleteById(id);
    }
}
