package com.pigeonchat.lab6.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProfileRequestDTO {
    private String username;
    private Date registrationDate;
    private String description;
    private String avatar;
    private String email;
}
