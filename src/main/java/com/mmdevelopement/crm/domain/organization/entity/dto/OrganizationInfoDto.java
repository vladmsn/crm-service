package com.mmdevelopement.crm.domain.organization.entity.dto;

import lombok.Builder;

@Builder
public record OrganizationInfoDto(
        String phoneNumber,
        String email,
        String image,
        String CUI,
        String regCom,
        String address,
        String city,
        String county,
        String country,
        String postalCode) {
}
