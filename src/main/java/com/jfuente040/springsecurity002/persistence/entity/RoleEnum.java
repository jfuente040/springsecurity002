package com.jfuente040.springsecurity002.persistence.entity;

public enum RoleEnum {
    ADMIN("ROLE_ADMIN", "Administrator"),
    USER("ROLE_USER", "User"),
    MODERATOR("ROLE_MODERATOR", "Moderator"),
    GUEST("ROLE_GUEST", "Guest");

    private final String roleName;
    private final String description;

    RoleEnum(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
