package com.ms.user.Enum;

public enum UserRole {

    ADMIN("ADMIN"),
    USER("USER");

    private String role;

    UserRole(String user) {
        this.role = user;
    }
}
