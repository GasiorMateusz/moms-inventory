package com.codecool.dadsinventory.security;

public enum UserPermission {
    PRIVACY("privacy:read"),
    DETAILS("details:read"),
    SEARCH("search:write"),
    READER("items:read"),
    EDITOR("items:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }


    public String getPermission() {
        return permission;
    }
}
