package com.pigeonchat.lab6.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column
    private Date birthday;

    @Column(nullable = false)
    private Date registrationDate;

    @Column
    private String description;

    @Column(nullable = false)
    private String avatar;

    @Column
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole = UserRole.USER;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Chat> chats;
}
