package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.ElementEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ElementDto {

        private Integer id;

        private String name;

        private String type;

        private String description;

        private Float acquisitionPrice;

        private Float sellingPrice;

        private Integer categoryId;

        private String categoryName;

        private Integer taxId;

        private Float taxValue;

        public static ElementDto toDto(ElementEntity elementEntity) {
            return new ElementDto()
                    .setId(elementEntity.id())
                    .setName(elementEntity.name())
                    .setDescription(elementEntity.description())
                    .setAcquisitionPrice(elementEntity.acquisitionPrice())
                    .setSellingPrice(elementEntity.sellingPrice())
                    .setCategoryId(elementEntity.categoryId())
                    .setTaxId(elementEntity.taxId());
        }

        public ElementEntity toEntity() {
            return new ElementEntity()
                    .id(getId())
                    .name(getName())
                    .description(getDescription())
                    .acquisitionPrice(getAcquisitionPrice())
                    .sellingPrice(getSellingPrice())
                    .categoryId(getCategoryId())
                    .taxId(getTaxId());
        }
}
