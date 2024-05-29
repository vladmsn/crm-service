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
@Table(name = "tax")
public class TaxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "rate")
    private Float rate;

    @Column(name = "deleted")
    private Boolean deleted;
}
