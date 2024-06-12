package com.mmdevelopement.crm.reporting.entity.items;

import lombok.Builder;

@Builder
public record CashFlowItem (
        String month,
        Integer year,
        Float income,
        Float expenses
){
}
