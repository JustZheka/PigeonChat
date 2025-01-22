package com.pigeonchat.lab6.repository;

import com.pigeonchat.lab6.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    List<Profile> findByUsername(String username);
}