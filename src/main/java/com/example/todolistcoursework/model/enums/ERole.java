package com.example.todolistcoursework.model.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum ERole implements GrantedAuthority {
    USER,
    PREMIUM,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
