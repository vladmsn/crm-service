package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoicePaymentEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true, fluent = true)
public class InvoicePaymentDto {

    private Integer id;
    private Integer invoiceId;
    private Integer partnerId;
    private String partnerName;
    private String partnerCUI;

    private Float amount;
    private String direction;
    private Integer bankAccountId;
    private String paymentMethod;
    private String description;
    private Date paymentDate;

    public static InvoicePaymentDto fromEntity(InvoicePaymentEntity invoicePaymentEntity, String partnerName,
                                               String partnerCUI, String direction) {
        return new InvoicePaymentDto()
                .id(invoicePaymentEntity.id())
                .invoiceId(invoicePaymentEntity.invoiceId())
                .partnerId(invoicePaymentEntity.partnerId())
                .partnerName(partnerName)
                .partnerCUI(partnerCUI)
                .bankAccountId(invoicePaymentEntity.bankAccountId())
                .amount(invoicePaymentEntity.amount())
                .direction(direction)
                .paymentMethod(invoicePaymentEntity.paymentMethod())
                .description(invoicePaymentEntity.description())
                .paymentDate(invoicePaymentEntity.paymentDate());
    }

    public InvoicePaymentEntity toEntity() {
        return new InvoicePaymentEntity()
                .id(id)
                .invoiceId(invoiceId)
                .partnerId(partnerId)
                .bankAccountId(bankAccountId)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .description(description)
                .paymentDate(paymentDate);
    }
}
