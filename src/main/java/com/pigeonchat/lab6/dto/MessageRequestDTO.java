package com.pigeonchat.lab6.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageRequestDTO {
    private String text;

    private UUID chatId;

    private UUID profileId;
}
