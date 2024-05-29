package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.ElementEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
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
                    .id(elementEntity.id())
                    .name(elementEntity.name())
                    .description(elementEntity.description())
                    .acquisitionPrice(elementEntity.acquisitionPrice())
                    .sellingPrice(elementEntity.sellingPrice())
                    .categoryId(elementEntity.categoryId())
                    .taxId(elementEntity.taxId());
        }

        public static ElementEntity toEntity(ElementDto elementDto) {
            return new ElementEntity()
                    .id(elementDto.id())
                    .name(elementDto.name())
                    .description(elementDto.description())
                    .acquisitionPrice(elementDto.acquisitionPrice())
                    .sellingPrice(elementDto.sellingPrice())
                    .categoryId(elementDto.categoryId())
                    .taxId(elementDto.taxId());
        }
}
