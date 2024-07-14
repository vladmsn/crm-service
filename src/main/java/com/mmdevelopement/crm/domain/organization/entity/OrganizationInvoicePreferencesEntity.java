package com.mmdevelopement.crm.domain.organization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
@Entity
@Table(name = "organization_invoice_preferences", schema = "public")
public class OrganizationInvoicePreferencesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "starting_number")
    private Integer startingNumber;

    @Column(name = "notes")
    private String notes;

    @Column(name = "footer")
    private String footer;

    @Column(name = "sub_header")
    private String subHeader;
}
