package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.enums.InvoiceDirection;
import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoicePaymentEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true)
public class InvoicePaymentDto {

    private Integer id;
    private Integer invoiceId;
    private String reference;
    private Integer partnerId;
    private String partnerName;
    private String partnerCUI;

    private Integer categoryId;
    private String categoryName;

    private Float amount;
    private InvoiceDirection direction;
    private Integer bankAccountId;
    private String paymentMethod;
    private InvoiceDirection paymentDirection;
    private String description;
    private Date paymentDate;

    public static InvoicePaymentDto fromEntity(InvoicePaymentEntity invoicePaymentEntity, String partnerName, Integer partnerId,
                                               String partnerCUI, String categoryName, InvoiceDirection direction) {
        return new InvoicePaymentDto()
                .setId(invoicePaymentEntity.id())
                .setInvoiceId(invoicePaymentEntity.invoiceId())
                .setReference(invoicePaymentEntity.reference())
                .setPartnerId(partnerId)
                .setPartnerName(partnerName)
                .setPartnerCUI(partnerCUI)
                .setCategoryId(invoicePaymentEntity.categoryId())
                .setCategoryName(categoryName)
                .setAmount(invoicePaymentEntity.amount())
                .setDirection(direction)
                .setBankAccountId(invoicePaymentEntity.bankAccountId())
                .setPaymentMethod(invoicePaymentEntity.paymentMethod())
                .setPaymentDirection(InvoiceDirection.fromString(invoicePaymentEntity.paymentDirection()))
                .setDescription(invoicePaymentEntity.description())
                .setPaymentDate(invoicePaymentEntity.paymentDate());
    }

    public InvoicePaymentEntity toEntity() {
        return new InvoicePaymentEntity()
                .id(getId())
                .type(invoiceId == null ? "REF PAYMENT" : "INVOICE PAYMENT")
                .invoiceId(getInvoiceId())
                .reference(getReference())
                .categoryId(getCategoryId())
                .amount(getAmount())
                .bankAccountId(getBankAccountId())
                .paymentMethod(getPaymentMethod())
                .paymentDirection(getPaymentDirection().toString())
                .description(getDescription())
                .paymentDate(getPaymentDate());
    }
}
