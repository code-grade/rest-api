package com.codegrade.restapi.security;

import lombok.Getter;

@Getter
public enum UserPermission {
    READ("read"),
    WRITE("write");

    UserPermission(String name) {}
}
