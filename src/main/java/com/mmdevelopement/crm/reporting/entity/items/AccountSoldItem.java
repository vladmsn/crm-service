package com.mmdevelopement.crm.reporting.entity.items;

import lombok.Builder;

@Builder
public record AccountSoldItem (
        String accountName,
        Double totalSold
) {
}
