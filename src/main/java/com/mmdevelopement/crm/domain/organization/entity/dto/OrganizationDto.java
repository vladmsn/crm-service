package com.mmdevelopement.crm.domain.organization.entity.dto;

import lombok.Builder;


@Builder
public record OrganizationDto (Integer id,
                               String name,
                               String tenantId,
                               String license,
                               String colorCodeNavBar,
                               String colorLeftSideBar,
                               String font,
                               String dbSchemaName,
                               String status,
                               Integer version
                               ) {}
