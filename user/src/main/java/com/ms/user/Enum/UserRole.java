package com.ms.user.Enum;

public enum UserRole {

    ADMIN("admin"),
    USER("user");

    private String role;

    UserRole(String user) {
        this.role = user;
    }
}
