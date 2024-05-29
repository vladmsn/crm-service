package com.mmdevelopement.crm.domain.organization.entity.dto;

import lombok.Builder;

@Builder
public record OrganizationInfoDto(
        String telephone,
        String email,
        String image,
        String CUI,
        String address,
        String city,
        String county,
        String country,
        String postalCode) {
}
