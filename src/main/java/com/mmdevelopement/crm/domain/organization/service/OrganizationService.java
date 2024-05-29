/**
 * @author Vlad Muresan <mv.muresanvlad@gmail.com>
 */

package com.mmdevelopement.crm.domain.organization.service;

import com.mmdevelopement.crm.config.security.context.RequestContextHolder;
import com.mmdevelopement.crm.domain.organization.entity.OrganizationEntity;
import com.mmdevelopement.crm.domain.organization.entity.OrganizationInfoEntity;
import com.mmdevelopement.crm.domain.organization.entity.dto.OrganizationInfoDto;
import com.mmdevelopement.crm.domain.organization.entity.wrapper.OrganizationWrapper;
import com.mmdevelopement.crm.domain.organization.entity.dto.OrganizationDto;
import com.mmdevelopement.crm.domain.organization.repository.OrganizationInfoRepository;
import com.mmdevelopement.crm.domain.organization.repository.OrganizationRepository;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import com.mmdevelopement.crm.utils.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationInfoRepository organizationInfoRepository;

    public List<OrganizationDto> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(organizationEntity -> OrganizationDto.builder()
                        .name(organizationEntity.name())
                        .tenantId(organizationEntity.tenantId())
                        .license(organizationEntity.license())
                        .dbSchemaName(organizationEntity.dbSchemaName())
                        .build())
                .toList();
    }

    public List<OrganizationInfoDto> getAllOrganizationsInfo() {
        return organizationInfoRepository.findAll()
                .stream()
                .map(organizationInfoEntity -> OrganizationInfoDto.builder()
                        .telephone(organizationInfoEntity.telephone())
                        .email(organizationInfoEntity.email())
                        .image(organizationInfoEntity.image() != null ? "present, but omitted" : null)
                        .CUI(organizationInfoEntity.CUI())
                        .address(organizationInfoEntity.address())
                        .city(organizationInfoEntity.city())
                        .county(organizationInfoEntity.county())
                        .country(organizationInfoEntity.country())
                        .postalCode(organizationInfoEntity.postalCode())
                        .build())
                .toList();
    }

    public Optional<OrganizationDto> findOrganizationByTenantId(String tenantId) {
        OrganizationEntity organizationEntity = organizationRepository.findByTenantId(tenantId);

        if (organizationEntity == null) {
            return Optional.empty();
        }

        return Optional.of(OrganizationDto.builder()
                .name(organizationEntity.name())
                .tenantId(organizationEntity.tenantId())
                .license(organizationEntity.license())
                .dbSchemaName(organizationEntity.dbSchemaName())
                .status(organizationEntity.status())
                .build());
    }

    public OrganizationDto getCurrentOrganization() {
        String tenantId = RequestContextHolder.getCurrentTenantId();

        return findOrganizationByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
    }

    public OrganizationInfoDto getCurrentOrganizationInfo() {
        String tenantId = RequestContextHolder.getCurrentTenantId();

        OrganizationEntity organizationEntity = organizationRepository.findByTenantId(tenantId);
        OrganizationInfoEntity organizationInfoEntity = organizationInfoRepository.findByOrganizationId(organizationEntity.id());

        return OrganizationInfoDto.builder()
                .telephone(organizationInfoEntity.telephone())
                .email(organizationInfoEntity.email())
                .image(ImageUtils.encodeImage(organizationInfoEntity.image()))
                .CUI(organizationInfoEntity.CUI())
                .address(organizationInfoEntity.address())
                .city(organizationInfoEntity.city())
                .county(organizationInfoEntity.county())
                .country(organizationInfoEntity.country())
                .postalCode(organizationInfoEntity.postalCode())
                .build();

    }

    @Transactional
    public OrganizationDto createOrganization(OrganizationWrapper organizationWrapper) {

        OrganizationEntity organizationEntity = new OrganizationEntity()
                .name(organizationWrapper.getName())
                .tenantId(organizationWrapper.getTenantId())
                .dbSchemaName(organizationWrapper.getTenantId() + "_db")
                .status("FIRST_SYNC");

        organizationRepository.save(organizationEntity);

        OrganizationInfoEntity organizationInfoEntity = new OrganizationInfoEntity()
                .organizationId(organizationEntity.id())
                .telephone(organizationWrapper.getTelephone())
                .email(organizationWrapper.getEmail())
                .image(ImageUtils.decodeImage(organizationWrapper.getImage()))
                .CUI(organizationWrapper.getCUI())
                .address(organizationWrapper.getAddress())
                .city(organizationWrapper.getCity())
                .county(organizationWrapper.getState())
                .country(organizationWrapper.getCountry())
                .postalCode(organizationWrapper.getPostalCode());

        organizationInfoRepository.save(organizationInfoEntity);

        return OrganizationDto.builder()
                .name(organizationEntity.name())
                .tenantId(organizationEntity.tenantId())
                .license(organizationEntity.license())
                .dbSchemaName(organizationEntity.dbSchemaName())
                .build();
    }

    @Transactional
    public OrganizationDto updateOrganization(OrganizationWrapper organizationWrapper) {

        OrganizationEntity organizationEntity = organizationRepository.findByTenantId(organizationWrapper.getTenantId());
        organizationEntity.name(organizationWrapper.getName());
        organizationRepository.save(organizationEntity);

        OrganizationInfoEntity organizationInfoEntity = organizationInfoRepository.findByOrganizationId(organizationEntity.id());
        organizationInfoEntity.telephone(organizationWrapper.getTelephone());
        organizationInfoEntity.email(organizationWrapper.getEmail());
        organizationInfoEntity.image(ImageUtils.decodeImage(organizationWrapper.getImage()));
        organizationInfoEntity.CUI(organizationWrapper.getCUI());
        organizationInfoEntity.address(organizationWrapper.getAddress());
        organizationInfoEntity.city(organizationWrapper.getCity());
        organizationInfoEntity.county(organizationWrapper.getState());
        organizationInfoEntity.country(organizationWrapper.getCountry());
        organizationInfoEntity.postalCode(organizationWrapper.getPostalCode());
        organizationInfoRepository.save(organizationInfoEntity);

        return OrganizationDto.builder()
                .name(organizationEntity.name())
                .tenantId(organizationEntity.tenantId())
                .license(organizationEntity.license())
                .dbSchemaName(organizationEntity.dbSchemaName())
                .build();

    }

    public void updateOrganizationStatus(String tenantId, String status) {
        OrganizationEntity organizationEntity = organizationRepository.findByTenantId(tenantId);
        organizationEntity.status(status);
        organizationRepository.save(organizationEntity);
    }

}