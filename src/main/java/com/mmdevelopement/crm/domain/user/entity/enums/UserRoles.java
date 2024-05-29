package com.mmdevelopement.crm.domain.user.entity.enums;

public enum UserRoles {
    ROLE_SUPER_ADMIN("super_admin"),
    ROLE_ADMIN("admin"),
    ROLE_USER("user");

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getRoleString() {
        return role;
    }

    public static UserRoles getRoleFromRoleString(String role) {
        for (UserRoles userRole : UserRoles.values()) {
            if (userRole.role.equals(role)) {
                return userRole;
            }
        }

        throw new IllegalArgumentException("No role found for role: " + role);
    }
}
