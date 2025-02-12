package com.pigeonchat.lab6.repository;

import com.pigeonchat.lab6.entity.Chat;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    void deleteById(UUID chatId);

    Optional<Chat> findById(UUID chatId);

    boolean existsById(@NonNull UUID chatId);
}
