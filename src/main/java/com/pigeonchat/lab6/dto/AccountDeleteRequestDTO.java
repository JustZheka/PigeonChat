package com.pigeonchat.lab6.dto;

import lombok.Data;

@Data
public class AccountDeleteRequestDTO {
    private String login;

    private String password;
}
