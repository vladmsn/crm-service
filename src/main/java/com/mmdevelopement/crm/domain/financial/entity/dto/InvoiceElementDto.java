package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.ElementEntity;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true, fluent = true)
public class InvoiceElementDto {

    private Integer elementId;
    private String elementName;
    private Float elementPrice;
    private String elementDescription;

    private Integer taxId;
    private Float elementTax;

    private Integer quantity;

    public static InvoiceElementDto fromElement(ElementDto entity, Integer quantity, String direction, Float taxRate) {
        return new InvoiceElementDto()
                .elementId(entity.id())
                .elementName(entity.name())
                .elementPrice(direction.equals("in") ? entity.acquisitionPrice() : entity.sellingPrice())
                .elementDescription(entity.description())
                .quantity(quantity)
                .taxId(entity.taxId())
                .elementTax(taxRate);
    }
}
