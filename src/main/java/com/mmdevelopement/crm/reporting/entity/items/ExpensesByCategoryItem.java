package com.mmdevelopement.crm.reporting.entity.items;

import lombok.Builder;

@Builder
public record ExpensesByCategoryItem (
        String categoryName,
        String colorCode,
        Float expenses
) {
}
