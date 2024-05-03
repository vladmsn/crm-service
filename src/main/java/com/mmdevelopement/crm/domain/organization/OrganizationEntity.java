/**
 * @author Vlad Muresan <mv.muresanvlad@gmail.com>
 */

package com.mmdevelopement.crm.domain.organization;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "organization")
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "tenant_id", nullable = false, length = 200)
    private String tenantId;

    @Column(name = "db_schema_name", nullable = false, length = 200)
    private String dbSchemaName;

    @Column(name = "license", nullable = false, unique = true, updatable = false)
    private String license = UUID.randomUUID().toString();

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image")
    private byte[] image;
}
