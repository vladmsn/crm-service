package com.mmdevelopement.crm.domain.organization.repository;

import com.mmdevelopement.crm.domain.organization.entity.OrganizationInvoicePreferencesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationInvoicePreferencesRepository extends JpaRepository<OrganizationInvoicePreferencesEntity, Integer> {
    OrganizationInvoicePreferencesEntity findByOrganizationId(Integer organizationId);
}
