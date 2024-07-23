/**
 * @author Vlad Muresan <mv.muresanvlad@gmail.com>
 */

package com.mmdevelopement.crm.domain.organization.service;

import com.mmdevelopement.crm.config.security.context.RequestContextHolder;
import com.mmdevelopement.crm.domain.organization.entity.OrganizationEntity;
import com.mmdevelopement.crm.domain.organization.entity.OrganizationInfoEntity;
import com.mmdevelopement.crm.domain.organization.entity.OrganizationInvoicePreferencesEntity;
import com.mmdevelopement.crm.domain.organization.entity.dto.OrganizationInfoDto;
import com.mmdevelopement.crm.domain.organization.entity.dto.OrganizationInvoiceInfoDto;
import com.mmdevelopement.crm.domain.organization.entity.wrapper.OrganizationWrapper;
import com.mmdevelopement.crm.domain.organization.entity.dto.OrganizationDto;
import com.mmdevelopement.crm.domain.organization.repository.OrganizationInfoRepository;
import com.mmdevelopement.crm.domain.organization.repository.OrganizationInvoicePreferencesRepository;
import com.mmdevelopement.crm.domain.organization.repository.OrganizationRepository;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import com.mmdevelopement.crm.utils.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationInfoRepository organizationInfoRepository;
    private final OrganizationInvoicePreferencesRepository organizationInvoicePreferencesRepository;

    public List<OrganizationDto> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(organizationEntity -> OrganizationDto.builder()
                        .name(organizationEntity.name())
                        .tenantId(organizationEntity.tenantId())
                        .colorCodeNavBar(organizationEntity.colorCodeNavBar())
                        .colorLeftSideBar(organizationEntity.colorLeftSideBar())
                        .license(organizationEntity.license())
                        .dbSchemaName(organizationEntity.dbSchemaName())
                        .build())
                .toList();
    }

    public List<OrganizationInfoDto> getAllOrganizationsInfo() {
        return organizationInfoRepository.findAll()
                .stream()
                .map(organizationInfoEntity -> OrganizationInfoDto.builder()
                        .phoneNumber(organizationInfoEntity.phoneNumber())
                        .email(organizationInfoEntity.email())
                        .image(organizationInfoEntity.image() != null ? "present, but omitted" : null)
                        .cui(organizationInfoEntity.CUI())
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
                .id(organizationEntity.id())
                .name(organizationEntity.name())
                .tenantId(organizationEntity.tenantId())
                .license(organizationEntity.license())
                .dbSchemaName(organizationEntity.dbSchemaName())
                .colorCodeNavBar(organizationEntity.colorCodeNavBar())
                .colorLeftSideBar(organizationEntity.colorLeftSideBar())
                .version(1)
                .status(organizationEntity.status())
                .build());
    }

    public Optional<OrganizationInvoiceInfoDto> findOrganizationInvoiceInfoByTenantId(String tenantId) {
        OrganizationEntity organizationEntity = organizationRepository.findByTenantId(tenantId);
        OrganizationInvoicePreferencesEntity organizationInvoicePreferencesEntity = organizationInvoicePreferencesRepository.findByOrganizationId(organizationEntity.id());

        if (organizationInvoicePreferencesEntity == null) {
            return Optional.empty();
        }

        return Optional.of(OrganizationInvoiceInfoDto.builder()
                .id(organizationInvoicePreferencesEntity.id())
                .organizationId(organizationInvoicePreferencesEntity.organizationId())
                .prefix(organizationInvoicePreferencesEntity.prefix())
                .startingNumber(organizationInvoicePreferencesEntity.startingNumber())
                .build());
    }

    public OrganizationDto getCurrentOrganization() {
        log.info("Getting current organization for tenant: {}", RequestContextHolder.getCurrentTenantId());
        String tenantId = RequestContextHolder.getCurrentTenantId();

        return findOrganizationByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
    }

    public OrganizationInfoDto getCurrentOrganizationInfo() {
        log.info("Getting current organization info for tenant: {}", RequestContextHolder.getCurrentTenantId());

        String tenantId = RequestContextHolder.getCurrentTenantId();

        OrganizationEntity organizationEntity = organizationRepository.findByTenantId(tenantId);
        OrganizationInfoEntity organizationInfoEntity = organizationInfoRepository.findByOrganizationId(organizationEntity.id());

        return OrganizationInfoDto.builder()
                .phoneNumber(organizationInfoEntity.phoneNumber())
                .email(organizationInfoEntity.email())
                .image(organizationInfoEntity.image() != null ? ImageUtils.encodeImage(organizationInfoEntity.image()): null)
                .cui(organizationInfoEntity.CUI())
                .regCom(organizationInfoEntity.regCom())
                .address(organizationInfoEntity.address())
                .city(organizationInfoEntity.city())
                .county(organizationInfoEntity.county())
                .country(organizationInfoEntity.country())
                .postalCode(organizationInfoEntity.postalCode())
                .build();
    }

    public OrganizationInvoiceInfoDto getCurrentOrganizationInvoiceInfo() {
        String tenantId = RequestContextHolder.getCurrentTenantId();

        return findOrganizationInvoiceInfoByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization invoice info not found"));
    }

    public OrganizationInvoiceInfoDto updateCurrentOrganizationInvoiceInfo(OrganizationInvoiceInfoDto organizationInvoiceInfoDto) {
        String tenantId = RequestContextHolder.getCurrentTenantId();

        OrganizationEntity organizationEntity = organizationRepository.findByTenantId(tenantId);
        OrganizationInvoicePreferencesEntity organizationInvoicePreferencesEntity = organizationInvoicePreferencesRepository.findByOrganizationId(organizationEntity.id());

        organizationInvoicePreferencesEntity.prefix(organizationInvoiceInfoDto.prefix());
        organizationInvoicePreferencesEntity.startingNumber(organizationInvoiceInfoDto.startingNumber());
        organizationInvoicePreferencesEntity.notes(organizationInvoiceInfoDto.notes());
        organizationInvoicePreferencesEntity.footer(organizationInvoiceInfoDto.footer());
        organizationInvoicePreferencesEntity.subHeader(organizationInvoiceInfoDto.subHeader());
        organizationInvoicePreferencesRepository.save(organizationInvoicePreferencesEntity);

        return OrganizationInvoiceInfoDto.builder()
                .id(organizationInvoicePreferencesEntity.id())
                .organizationId(organizationInvoicePreferencesEntity.organizationId())
                .prefix(organizationInvoicePreferencesEntity.prefix())
                .startingNumber(organizationInvoicePreferencesEntity.startingNumber())
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
                .phoneNumber(organizationWrapper.getPhone())
                .email(organizationWrapper.getEmail())
                .image(ImageUtils.decodeImage(organizationWrapper.getImage()))
                .CUI(organizationWrapper.getCui())
                .address(organizationWrapper.getAddress())
                .city(organizationWrapper.getCity())
                .county(organizationWrapper.getCounty())
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
        organizationInfoEntity.phoneNumber(organizationWrapper.getPhone());
        organizationInfoEntity.email(organizationWrapper.getEmail());
        organizationInfoEntity.image(ImageUtils.decodeImage(organizationWrapper.getImage()));
        organizationInfoEntity.CUI(organizationWrapper.getCui());
        organizationInfoEntity.address(organizationWrapper.getAddress());
        organizationInfoEntity.city(organizationWrapper.getCity());
        organizationInfoEntity.county(organizationWrapper.getCounty());
        organizationInfoEntity.country(organizationWrapper.getCountry());
        organizationInfoEntity.postalCode(organizationWrapper.getPostalCode());
        organizationInfoRepository.save(organizationInfoEntity);

        return OrganizationDto.builder()
                .id(organizationEntity.id())
                .name(organizationEntity.name())
                .tenantId(organizationEntity.tenantId())
                .license(organizationEntity.license())
                .dbSchemaName(organizationEntity.dbSchemaName())
                .colorLeftSideBar(organizationEntity.colorLeftSideBar())
                .colorCodeNavBar(organizationEntity.colorCodeNavBar())
                .status(organizationEntity.status())
                .version(1)
                .build();
    }

    public void updateOrganizationStatus(String tenantId, String status) {
        OrganizationEntity organizationEntity = organizationRepository.findByTenantId(tenantId);
        organizationEntity.status(status);
        organizationRepository.save(organizationEntity);
    }
}