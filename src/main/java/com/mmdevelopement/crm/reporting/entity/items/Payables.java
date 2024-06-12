package com.mmdevelopement.crm.reporting.entity.items;

import lombok.Builder;

@Builder
public record Payables (
        Float totalToPay,
        Float totalOpen,
        Float totalOverdue
) {
}
