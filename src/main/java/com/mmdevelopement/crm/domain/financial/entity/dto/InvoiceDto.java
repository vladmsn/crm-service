package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.enums.InvoiceDirection;
import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoiceEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class InvoiceDto {

    private Integer id;

    private Float total;

    private Float remainingAmount;

    private Integer partnerId;

    private String partnerName;

    private Integer categoryId;

    private String categoryName;

    private String invoiceNumber;

    private String orderNumber;

    private InvoiceDirection direction;  // enum: IN or OUT <=> purchase or sale

    private Date invoiceDate;

    private Date dueDate;

    private Boolean completed;

    private List<InvoiceElementDto> elements;

    public static InvoiceDto toDto(InvoiceEntity invoiceEntity) {
        return new InvoiceDto()
                .setId(invoiceEntity.id())
                .setTotal(invoiceEntity.total())
                .setRemainingAmount(invoiceEntity.remainingAmount())
                .setPartnerId(invoiceEntity.partnerId())
                .setCategoryId(invoiceEntity.categoryId())
                .setInvoiceNumber(invoiceEntity.invoiceNumber())
                .setOrderNumber(invoiceEntity.orderNumber())
                .setDirection(invoiceEntity.direction())
                .setInvoiceDate(invoiceEntity.invoiceDate())
                .setDueDate(invoiceEntity.dueDate())
                .setCompleted(invoiceEntity.completed());
    }

    public InvoiceEntity toEntity() {
        return new InvoiceEntity()
                .id(getId())
                .total(getTotal())
                .remainingAmount(getRemainingAmount())
                .partnerId(getPartnerId())
                .categoryId(getCategoryId())
                .invoiceNumber(getInvoiceNumber())
                .orderNumber(getOrderNumber())
                .direction(getDirection())
                .invoiceDate(getInvoiceDate())
                .dueDate(getDueDate())
                .completed(getCompleted());
    }
}
