package com.mmdevelopement.crm.domain.financial.entity.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum InvoiceDirection {
    IN("IN"),
    OUT("OUT");

    private final String direction;

    InvoiceDirection(String direction) {
        this.direction = direction;
    }

    public static InvoiceDirection fromString(String direction) {
        for (InvoiceDirection d : InvoiceDirection.values()) {
            if (d.getDirection().equalsIgnoreCase(direction)) {
                return d;
            }
        }
        return null;
    }
}
