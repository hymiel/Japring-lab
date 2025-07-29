package com.domain.user.dto;

import com.domain.user.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;
    private boolean isAdmin;
    private String adminToken;

    public Role getRole() {
        return isAdmin ? Role.ADMIN : Role.USER;
    }
}
