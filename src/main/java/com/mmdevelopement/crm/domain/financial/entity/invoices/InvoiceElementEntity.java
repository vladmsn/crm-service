package com.mmdevelopement.crm.domain.financial.entity.invoices;

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
@Table(name = "invoice_element")
public class InvoiceElementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "element_id")
    private Integer elementId;

    @Column(name = "tax_id")
    private Integer taxId;

    @Column(name = "quantity")
    private Integer quantity;
}
