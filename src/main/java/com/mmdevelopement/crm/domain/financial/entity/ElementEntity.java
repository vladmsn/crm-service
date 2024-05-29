package com.mmdevelopement.crm.domain.financial.entity;

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
@Table(name = "element")
public class ElementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "acquisition_price")
    private Float acquisitionPrice;

    @Column(name = "selling_price")
    private Float sellingPrice;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "tax_id")
    private Integer taxId;

    @Column(name = "deleted")
    private Boolean deleted;
}
