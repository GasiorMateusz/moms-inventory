package com.codecool.dadsinventory.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public enum UserRole {
    DAD(List.of(UserPermission.DETAILS, UserPermission.READER)),
    SON(List.of(UserPermission.PRIVACY, UserPermission.DETAILS, UserPermission.EDITOR, UserPermission.READER)),
    MOM(List.of(UserPermission.PRIVACY));

    private final List<UserPermission> permissions;

    UserRole(List<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public List<UserPermission> getPermissions() {
        return permissions;
    }

    public List<SimpleGrantedAuthority> getGrantedAuthorities() {
        List<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

}
