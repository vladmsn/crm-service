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

    private Float amount;
    private InvoiceDirection direction;
    private Integer bankAccountId;
    private String paymentMethod;
    private String description;
    private Date paymentDate;

    public static InvoicePaymentDto fromEntity(InvoicePaymentEntity invoicePaymentEntity, String partnerName,
                                               String partnerCUI, InvoiceDirection direction) {
        return new InvoicePaymentDto()
                .setId(invoicePaymentEntity.id())
                .setInvoiceId(invoicePaymentEntity.invoiceId())
                .setPartnerId(invoicePaymentEntity.partnerId())
                .setPartnerName(partnerName)
                .setPartnerCUI(partnerCUI)
                .setAmount(invoicePaymentEntity.amount())
                .setDirection(direction)
                .setBankAccountId(invoicePaymentEntity.bankAccountId())
                .setPaymentMethod(invoicePaymentEntity.paymentMethod())
                .setDescription(invoicePaymentEntity.description())
                .setPaymentDate(invoicePaymentEntity.paymentDate());
    }

    public InvoicePaymentEntity toEntity() {
        return new InvoicePaymentEntity()
                .id(getId())
                .invoiceId(getInvoiceId())
                .partnerId(getPartnerId())
                .amount(getAmount())
                .bankAccountId(getBankAccountId())
                .paymentMethod(getPaymentMethod())
                .description(getDescription())
                .paymentDate(getPaymentDate());
    }
}
