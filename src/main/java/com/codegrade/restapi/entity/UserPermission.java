package com.codegrade.restapi.entity;

import lombok.Getter;

@Getter
public enum UserPermission {
    READ("read"),
    WRITE("write");

    UserPermission(String name) {}
}
