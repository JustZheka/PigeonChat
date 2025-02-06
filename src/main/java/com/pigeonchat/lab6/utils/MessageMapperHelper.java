package com.pigeonchat.lab6.utils;

import com.pigeonchat.lab6.entity.Message;
import com.pigeonchat.lab6.entity.Profile;
import com.pigeonchat.lab6.repository.MessageRepository;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MessageMapperHelper {
    private final MessageRepository messageRepository;

    public MessageMapperHelper(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Named("mapMessageToIds")
    public List<UUID> mapMessageToIds(List<Message> entities) {
        return entities == null ? Collections.emptyList() :
                entities.stream()
                        .map(Message::getId)
                        .collect(Collectors.toList());
    }

    @Named("mapIdsToMessages")
    public List<Message> mapIdsToMessages(List<UUID> ids) {
        return ids == null ? Collections.emptyList() :
                ids.stream()
                        .map(messageRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
    }
}
