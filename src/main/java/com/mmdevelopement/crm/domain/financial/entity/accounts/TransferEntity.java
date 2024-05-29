package com.mmdevelopement.crm.domain.financial.entity.accounts;

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
@Table(name = "transfer")
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "from_bank_account_id")
    private Integer fromBankAccountId;

    @Column(name = "to_bank_account_id")
    private Integer toBankAccountId;

    @Column(name = "transfer_date")
    private Date transferDate;

    @Column(name = "description")
    private String description;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "reference")
    private String reference;
}
