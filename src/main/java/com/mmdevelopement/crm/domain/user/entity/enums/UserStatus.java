package com.mmdevelopement.crm.domain.user.entity.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    public static UserStatus fromString(String status) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.getStatus().equalsIgnoreCase(status)) {
                return userStatus;
            }
        }
        return null;
    }
}
