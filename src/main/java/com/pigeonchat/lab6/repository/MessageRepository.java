package com.pigeonchat.lab6.repository;

import com.pigeonchat.lab6.entity.Message;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatId(UUID chatId);

    Optional<Message> findById(UUID id);

    void deleteById(UUID messageId);

    boolean existsById(@NonNull UUID messageId);
}
