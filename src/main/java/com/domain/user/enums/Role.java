package com.domain.user.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", 1),
    ADMIN("ROLE_ADMIN",2);

    private final String k;
    private final Integer v;
}
