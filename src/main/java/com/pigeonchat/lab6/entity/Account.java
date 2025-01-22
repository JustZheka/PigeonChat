package com.pigeonchat.lab6.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "accounts")
public class Account {
    @Id
    private String login;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private Profile profile;
}
