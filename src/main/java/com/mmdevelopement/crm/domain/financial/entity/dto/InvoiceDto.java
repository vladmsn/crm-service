package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoiceEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true, fluent = true)
public class InvoiceDto {

    private Integer id;

    private Float total;

    private Integer partnerId;

    private String partnerName;

    private Integer categoryId;

    private String categoryName;

    private String invoiceNumber;

    private String orderNumber;

    private String direction;  // in or out

    private Date invoiceDate;

    private Date dueDate;

    private Boolean completed;

    public static InvoiceDto toDto(InvoiceEntity invoiceEntity) {
        return new InvoiceDto()
                .id(invoiceEntity.id())
                .total(invoiceEntity.total())
                .partnerId(invoiceEntity.partnerId())
                .categoryId(invoiceEntity.categoryId())
                .invoiceNumber(invoiceEntity.invoiceNumber())
                .orderNumber(invoiceEntity.orderNumber())
                .direction(invoiceEntity.direction())
                .invoiceDate(invoiceEntity.invoiceDate())
                .dueDate(invoiceEntity.dueDate())
                .completed(invoiceEntity.completed());
    }

    public static InvoiceEntity toEntity(InvoiceDto invoiceDto) {
        return new InvoiceEntity()
                .id(invoiceDto.id())
                .total(invoiceDto.total())
                .partnerId(invoiceDto.partnerId())
                .categoryId(invoiceDto.categoryId())
                .invoiceNumber(invoiceDto.invoiceNumber())
                .orderNumber(invoiceDto.orderNumber())
                .direction(invoiceDto.direction())
                .invoiceDate(invoiceDto.invoiceDate())
                .dueDate(invoiceDto.dueDate())
                .completed(invoiceDto.completed());
    }
}
