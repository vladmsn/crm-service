/**
 * @author Vlad Muresan <mv.muresanvlad@gmail.com>
 */

package com.mmdevelopement.crm.domain.organization.repository;

import com.mmdevelopement.crm.domain.organization.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Integer> {
    OrganizationEntity findByTenantId(String tenantId);
}
