/**
 * @author Vlad Muresan <mv.muresanvlad@gmail.com>
 */

package com.mmdevelopement.crm.domain.organization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public List<OrganizationEntity> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<OrganizationEntity> findOrganizationByTenantId(String tenantId) {
        return Optional.ofNullable(organizationRepository.findByTenantId(tenantId));
    }

}
