package com.pigeonchat.lab6.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class MessageResponseDTO {
    private UUID id;

    private String text;

    private ProfileResponseDTO profile;

    private ChatResponseDTO chat;
}
