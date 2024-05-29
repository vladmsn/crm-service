package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.TaxEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class TaxDto {

        private Integer id;

        private String name;

        private String type;

        private Float value;

        private Boolean deleted;

        public static TaxDto toDto(TaxEntity taxEntity) {
            return new TaxDto()
                    .id(taxEntity.id())
                    .name(taxEntity.name())
                    .type(taxEntity.type())
                    .value(taxEntity.rate())
                    .deleted(taxEntity.deleted());
        }

        public static TaxEntity toEntity(TaxDto taxDto) {
            return new TaxEntity()
                    .id(taxDto.id())
                    .name(taxDto.name())
                    .type(taxDto.type())
                    .rate(taxDto.value)
                    .deleted(taxDto.deleted());
        }
}
