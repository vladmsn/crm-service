package com.mmdevelopement.crm.domain.organization.entity;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
@Entity
@Table(name = "organization_info", schema = "public")
public class OrganizationInfoEntity {

    @Id
    private Integer id;

    @Column(name = "organization_id", nullable = false)
    private Integer organizationId;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image", columnDefinition="bytea")
    private byte[] image;

    @Column(name = "CUI", length = 20, nullable = false, unique = true)
    private String CUI;

    @Column(name = "reg_com", length = 20, nullable = false, unique = true)
    private String regCom;

    @Column(name = "address", length = 200, nullable = false)
    private String address;

    @Column(name = "city", length = 100, nullable = false)
    private String city;

    @Column(name = "county", length = 100, nullable = false)
    private String county;

    @Column(name = "country", length = 100, nullable = false)
    private String country;

    @Column(name = "postal_code", length = 20, nullable = false)
    private String postalCode;
}
