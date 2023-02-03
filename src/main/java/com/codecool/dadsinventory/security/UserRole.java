package com.codecool.dadsinventory.security;

import com.codecool.dadsinventory.app.AppSimpleGrantedAuthority;
import com.codecool.dadsinventory.app.AppUserDetails;

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

    public List<AppSimpleGrantedAuthority> getGrantedAuthorities(AppUserDetails user) {
        List<AppSimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new AppSimpleGrantedAuthority(permission.getPermission(), user))
                .collect(Collectors.toList());
        permissions.add(new AppSimpleGrantedAuthority("ROLE_" + this.name(), user));
        return permissions;
    }

}
