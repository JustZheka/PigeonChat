package com.pigeonchat.lab6.controllers;

import com.pigeonchat.lab6.dto.ProfileRequestDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController{
    @Autowired
    private ProfileService profileService;

    @GetMapping
    public List<ProfileResponseDTO> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ProfileResponseDTO getProfileById(@PathVariable UUID id) {
        return profileService.getProfileById(id);
    }

    @GetMapping("/username/{username}")
    public List<ProfileResponseDTO> getProfileByUsername(@PathVariable String username) {
        return profileService.getProfileByUsername(username);
    }

    @PostMapping("/create")
    public ProfileResponseDTO createProfile(@RequestBody ProfileRequestDTO profileDTO) {
        return profileService.createProfile(profileDTO);
    }

    @PutMapping("/{id}")
    public ProfileResponseDTO updateProfile(@PathVariable UUID id, @RequestBody ProfileRequestDTO profileDTO) {
        return profileService.updateProfile(id, profileDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable UUID id) {
        profileService.deleteProfile(id);
    }
}
