package com.example.GameStore.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String address;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role roles;

    private String resetToken;

    private LocalDateTime resetTokenExpiryTime;
}
