package com.mmdevelopement.crm.reporting.entity.items;

import lombok.Builder;

@Builder
public record IncomeByCategoryItem (
        String categoryName,
        String colorCode,
        Float income
) {
}
