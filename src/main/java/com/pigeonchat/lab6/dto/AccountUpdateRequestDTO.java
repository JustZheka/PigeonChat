package com.pigeonchat.lab6.dto;

import lombok.Data;

@Data
public class AccountUpdateRequestDTO {
    private String login;

    private String oldPassword;

    private String newPassword;
}
