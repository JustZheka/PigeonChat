package com.pigeonchat.lab6.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class ChatResponseDTO {
    private UUID id;
    private String title;
    private String avatar;

    private List<MessageResponseDTO> messages;

    private List<ProfileResponseDTO> profiles;
}
