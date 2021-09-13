package com.codegrade.restapi.entity;

import com.google.common.collect.Sets;
import java.util.Set;

public enum UserRole {
    ADMIN(Sets.newHashSet(UserPermission.READ, UserPermission.WRITE)),
    INSTRUCTOR(Sets.newHashSet(UserPermission.READ, UserPermission.WRITE)),
    STUDENT(Sets.newHashSet(UserPermission.READ, UserPermission.WRITE));

    UserRole(Set<UserPermission> permissions) {}

    public String getRoleName() {
        return "ROLE_" + this.name();
    }
}
