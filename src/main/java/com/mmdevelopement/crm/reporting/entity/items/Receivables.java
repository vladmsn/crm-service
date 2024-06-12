package com.mmdevelopement.crm.reporting.entity.items;

import lombok.Builder;

@Builder
public record Receivables (
        Float totalToReceive,
        Float totalOpen,
        Float totalOverdue
) {
}
