package com.mmdevelopement.crm.domain.financial.entity.invoices;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true, fluent = true)
@Entity
@Table(name = "invoice")
public class InvoicePaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "partner_id")
    private Integer partnerId;

    @Column(name = "bank_account_id")
    private Integer bankAccountId;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "description")
    private String description;

    @Column(name = "payment_date")
    private Date paymentDate;
}
