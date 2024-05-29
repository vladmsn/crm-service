package com.mmdevelopement.crm.domain.organization.repository;

import com.mmdevelopement.crm.domain.organization.entity.OrganizationInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationInfoRepository extends JpaRepository<OrganizationInfoEntity, Integer> {
    OrganizationInfoEntity findByOrganizationId(Integer organizationId);
}
