package com.mmdevelopement.crm.domain.organization.entity.dto;

import lombok.Builder;


@Builder
public record OrganizationDto (String name,
                               String tenantId,
                               String license,
                               String colorCode,
                               String dbSchemaName,
                               String status) {}
