package com.pigeonchat.lab6.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
public class ProfileResponseDTO {
    private UUID id;
    private String username;
    private LocalDate birthday;
    private Date registrationDate;
    private String description;
    private String avatar;
    private String email;
    private String userRole;
}
