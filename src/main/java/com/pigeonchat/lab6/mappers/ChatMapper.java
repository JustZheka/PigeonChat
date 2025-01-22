package com.pigeonchat.lab6.mappers;

import com.pigeonchat.lab6.dto.ChatRequestDTO;
import com.pigeonchat.lab6.dto.ChatResponseDTO;
import com.pigeonchat.lab6.entity.Chat;
import org.modelmapper.ModelMapper;


public class ChatMapper extends BaseMapper<ChatRequestDTO, ChatResponseDTO, Chat> {
    private final ModelMapper modelMapper = new ModelMapper();

    public Chat toEntity(ChatResponseDTO responseDTO) {
        return modelMapper.map(responseDTO, Chat.class);
    }
}
