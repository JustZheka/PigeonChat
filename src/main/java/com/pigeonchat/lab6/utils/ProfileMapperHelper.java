package com.pigeonchat.lab6.utils;

import com.pigeonchat.lab6.entity.Profile;
import com.pigeonchat.lab6.repository.ProfileRepository;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProfileMapperHelper {
    private final ProfileRepository profileRepository;

    public ProfileMapperHelper(ProfileRepository profileMapperHelper) {
        this.profileRepository = profileMapperHelper;
    }

    @Named("mapProfileToIds")
    public List<UUID> mapProfileToIds(List<Profile> entities) {
        return entities == null ? Collections.emptyList() :
                entities.stream()
                        .map(Profile::getId)
                        .collect(Collectors.toList());
    }

    @Named("mapIdsToProfiles")
    public List<Profile> mapIdsToProfiles(List<UUID> ids) {
        return ids == null ? Collections.emptyList() :
                ids.stream()
                        .map(profileRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
    }
}
