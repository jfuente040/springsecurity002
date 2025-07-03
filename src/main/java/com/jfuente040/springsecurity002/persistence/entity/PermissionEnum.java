package com.jfuente040.springsecurity002.persistence.entity;

public enum PermissionEnum {
    // User permissions
    READ_USER("READ_USER", "Can read user information"),
    CREATE_USER("CREATE_USER", "Can create new users"),
    UPDATE_USER("UPDATE_USER", "Can update user information"),
    DELETE_USER("DELETE_USER", "Can delete users"),
    
    // Role permissions
    READ_ROLE("READ_ROLE", "Can read role information"),
    CREATE_ROLE("CREATE_ROLE", "Can create new roles"),
    UPDATE_ROLE("UPDATE_ROLE", "Can update role information"),
    DELETE_ROLE("DELETE_ROLE", "Can delete roles"),
    
    // Permission permissions
    READ_PERMISSION("READ_PERMISSION", "Can read permission information"),
    CREATE_PERMISSION("CREATE_PERMISSION", "Can create new permissions"),
    UPDATE_PERMISSION("UPDATE_PERMISSION", "Can update permission information"),
    DELETE_PERMISSION("DELETE_PERMISSION", "Can delete permissions"),
    
    // System permissions
    ADMIN_ACCESS("ADMIN_ACCESS", "Full administrative access"),
    MODERATOR_ACCESS("MODERATOR_ACCESS", "Moderator level access"),
    USER_ACCESS("USER_ACCESS", "Basic user access"),
    
    // Content permissions
    READ_CONTENT("READ_CONTENT", "Can read content"),
    CREATE_CONTENT("CREATE_CONTENT", "Can create content"),
    UPDATE_CONTENT("UPDATE_CONTENT", "Can update content"),
    DELETE_CONTENT("DELETE_CONTENT", "Can delete content"),
    PUBLISH_CONTENT("PUBLISH_CONTENT", "Can publish content"),
    
    // Report permissions
    VIEW_REPORTS("VIEW_REPORTS", "Can view reports"),
    GENERATE_REPORTS("GENERATE_REPORTS", "Can generate reports"),
    EXPORT_DATA("EXPORT_DATA", "Can export data");

    private final String permissionName;
    private final String description;

    PermissionEnum(String permissionName, String description) {
        this.permissionName = permissionName;
        this.description = description;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return permissionName;
    }
}
