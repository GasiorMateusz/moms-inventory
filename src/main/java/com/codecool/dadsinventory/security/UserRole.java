package com.codecool.dadsinventory.security;

import java.util.Set;

public enum UserRole {
    DAD(Set.of(UserPermission.DETAILS)),
    SON(Set.of(UserPermission.PRIVACY, UserPermission.DETAILS)),
    MOM(Set.of(UserPermission.PRIVACY));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    Set<UserPermission> getPermissions() {
        return permissions;
    }

}
