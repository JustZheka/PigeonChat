package com.pigeonchat.lab6.services;

import com.pigeonchat.lab6.dto.ProfileRequestDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.entity.Profile;
import com.pigeonchat.lab6.mappers.ProfileMapper;
import com.pigeonchat.lab6.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    private final ProfileMapper profileMapper = new ProfileMapper();

    public List<ProfileResponseDTO> getAllProfiles() {
        return profileRepository.findAll().stream()
                .map(x -> profileMapper.toResponseDTO(x, ProfileResponseDTO.class))
                .collect(Collectors.toList());
    }

    public ProfileResponseDTO getProfileById(UUID id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Профиль не найден"));
        return profileMapper.toResponseDTO(profile, ProfileResponseDTO.class);
    }

    public List<ProfileResponseDTO> getProfileByUsername(String username) {
        List<Profile> profiles = profileRepository.findByUsername(username);
        return profiles.stream()
                .map(x -> profileMapper.toResponseDTO(x, ProfileResponseDTO.class))
                .collect(Collectors.toList());
    }

    public ProfileResponseDTO createProfile(ProfileRequestDTO profileDTO) {
        Profile profile = profileMapper.toEntity(profileDTO, Profile.class);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toResponseDTO(savedProfile, ProfileResponseDTO.class);
    }

    public ProfileResponseDTO updateProfile(UUID id, ProfileRequestDTO profileDTO) {
        Profile existingProfile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Профиль не найден"));
        existingProfile.setUsername(profileDTO.getUsername());
        existingProfile.setRegistrationDate(profileDTO.getRegistrationDate());
        existingProfile.setDescription(profileDTO.getDescription());
        existingProfile.setAvatar(profileDTO.getAvatar());
        existingProfile.setEmail(profileDTO.getEmail());
        Profile updatedProfile = profileRepository.save(existingProfile);
        return profileMapper.toResponseDTO(updatedProfile, ProfileResponseDTO.class);
    }

    @Transactional
    public void deleteProfile(UUID id) {
        profileRepository.deleteById(id);
    }
}
