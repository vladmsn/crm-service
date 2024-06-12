package com.mmdevelopement.crm.domain.financial.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class InvoiceElementDto {

    private Integer elementId;
    private String elementName;
    private String elementDescription;

    private Float elementPrice;
    private Float elementTax;
    private Integer quantity;

    public static InvoiceElementDto fromElement(ElementDto elementDto, Integer quantity, Float elementPrice, Float taxRate) {
        return new InvoiceElementDto()
                .setElementId(elementDto.getId())
                .setElementName(elementDto.getName())
                .setElementPrice(elementPrice)
                .setElementDescription(elementDto.getDescription())
                .setQuantity(quantity)
                .setElementTax(taxRate);
    }
}
