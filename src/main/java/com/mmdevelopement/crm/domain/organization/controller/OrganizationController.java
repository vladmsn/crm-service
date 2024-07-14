package com.mmdevelopement.crm.domain.organization.controller;

import com.mmdevelopement.crm.domain.organization.entity.dto.OrganizationInfoDto;
import com.mmdevelopement.crm.domain.organization.entity.dto.OrganizationInvoiceInfoDto;
import com.mmdevelopement.crm.domain.organization.entity.wrapper.OrganizationWrapper;
import com.mmdevelopement.crm.domain.organization.entity.dto.OrganizationDto;
import com.mmdevelopement.crm.domain.organization.service.OrganizationService;

import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/")
    public OrganizationDto getCurrentOrganization() {
        return organizationService.getCurrentOrganization();
    }

    @GetMapping("/info")
    public OrganizationInfoDto getCurrentOrganizationInfo() {
        return organizationService.getCurrentOrganizationInfo();
    }

    @GetMapping("/invoice-preferences")
    public OrganizationInvoiceInfoDto getCurrentOrganizationInvoicePreferences() {
        return organizationService.getCurrentOrganizationInvoiceInfo();
    }

    @PutMapping("/invoice-preferences")
    public OrganizationInvoiceInfoDto updateCurrentOrganizationInvoicePreferences(@RequestBody OrganizationInvoiceInfoDto organizationInvoiceInfoDto) {
        return organizationService.updateCurrentOrganizationInvoiceInfo(organizationInvoiceInfoDto);
    }

    @GetMapping("/find")
    @Secured("ROLE_SUPER_ADMIN")
    public OrganizationDto getOrganizationByTenantId(@RequestParam String tenantId) {
        return organizationService.findOrganizationByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
    }

    @GetMapping("/all")
    @Secured("ROLE_SUPER_ADMIN")
    public List<OrganizationDto> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @GetMapping("/all/info")
    @Secured("ROLE_SUPER_ADMIN")
    public List<OrganizationInfoDto> getAllOrganizationsInfo() {
        return organizationService.getAllOrganizationsInfo();
    }

    @PostMapping("/")
    @Secured("ROLE_SUPER_ADMIN")
    public OrganizationDto createOrganization(@RequestBody OrganizationWrapper organizationWrapper) {
        return organizationService.createOrganization(organizationWrapper);
    }

    @PutMapping("/")
    @Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
    public OrganizationDto updateOrganization(@RequestBody OrganizationWrapper organizationWrapper) {
        return organizationService.updateOrganization(organizationWrapper);
    }

}
