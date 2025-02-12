package com.pigeonchat.lab6.controllers;

import com.pigeonchat.lab6.dto.ProfileRequestDTO;
import com.pigeonchat.lab6.dto.ProfileResponseDTO;
import com.pigeonchat.lab6.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController{
    final ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<ProfileResponseDTO>> getAllProfiles() {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getAllProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> getProfileById(@PathVariable final UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getProfileById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<ProfileResponseDTO>> getProfileByUsername(@PathVariable final String username) {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getProfileByUsername(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> updateProfile(@PathVariable final UUID id, @RequestBody ProfileRequestDTO profileDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.updateProfile(id, profileDTO));
    }
}
