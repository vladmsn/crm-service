package com.mmdevelopement.crm.domain.organization.entity.wrapper;

import lombok.Data;

@Data
public class OrganizationWrapper {
    private String name;
    private String tenantId;

    private String telephone;
    private String email;
    private String image;

    private String CUI;

    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
}
