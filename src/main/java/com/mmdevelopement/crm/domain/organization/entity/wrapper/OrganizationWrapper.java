package com.mmdevelopement.crm.domain.organization.entity.wrapper;

import lombok.Data;

@Data
public class OrganizationWrapper {
    private String name;
    private String tenantId;

    private String phone;
    private String email;
    private String image;

    private String cui;

    private String address;
    private String city;
    private String county;
    private String country;
    private String postalCode;

    private String colorCodeNavBar;
    private String colorLeftBar;
    private String font;
}
