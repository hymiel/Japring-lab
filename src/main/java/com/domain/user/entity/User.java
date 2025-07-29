package com.domain.user.entity;

import com.domain.user.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.awt.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")

public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    private Role role;


    @Builder
    public User(String email, String username, String password, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateEmail(String newEmail){
        this.email = newEmail;
    }
}
