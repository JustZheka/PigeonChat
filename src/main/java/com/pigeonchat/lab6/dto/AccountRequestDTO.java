package com.pigeonchat.lab6.dto;

import lombok.Data;

@Data
public class AccountRequestDTO {
    private String login;

    private String password;

    private ProfileRequestDTO profile;
}
