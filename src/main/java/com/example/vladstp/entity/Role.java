package com.example.vladstp.entity;

import org.springframework.security.core.GrantedAuthority;

//enam - перечисление
public /*class*/enum Role implements GrantedAuthority {
    USER, ADMIN; //6й добавили роль ADMIN

    @Override
    public String getAuthority() {
        return name();
    }
}
