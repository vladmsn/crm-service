package com.mmdevelopement.crm.domain.financial.entity.enums;

import lombok.Getter;

@Getter
public enum CategoryType {
    CHARGE("cheltuiala"),
    INCOME("venit"),
    ELEMENT("element"),
    OTHER("alta");

    private final String type;

    CategoryType(String type) {
        this.type = type;
    }

    public static CategoryType fromString(String type) {
        for (CategoryType t : CategoryType.values()) {
            if (t.getType().equalsIgnoreCase(type)) {
                return t;
            }
        }
        return null;
    }

    public static String fromEnum(CategoryType type) {
        return type.getType();
    }
}
