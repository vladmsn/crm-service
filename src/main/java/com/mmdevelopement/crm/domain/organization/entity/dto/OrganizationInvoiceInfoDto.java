package com.mmdevelopement.crm.domain.organization.entity.dto;

import lombok.Builder;

@Builder
public record OrganizationInvoiceInfoDto (
        Integer id,
        Integer organizationId,
        String prefix,
        Integer startingNumber) {
}
