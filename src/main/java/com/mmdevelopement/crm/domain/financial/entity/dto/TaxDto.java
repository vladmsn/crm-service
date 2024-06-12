package com.mmdevelopement.crm.domain.financial.entity.dto;


import com.mmdevelopement.crm.domain.financial.entity.TaxEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TaxDto {

        private Integer id;

        private String name;

        private String type;

        private Float value;

        private Boolean deleted;

        public static TaxDto toDto(TaxEntity taxEntity) {
            return new TaxDto()
                    .setId(taxEntity.id())
                    .setName(taxEntity.name())
                    .setType(taxEntity.type())
                    .setValue(taxEntity.rate())
                    .setDeleted(taxEntity.deleted());
        }

        public static TaxEntity toEntity(TaxDto taxDto) {
            return new TaxEntity()
                    .id(taxDto.getId())
                    .name(taxDto.getName())
                    .type(taxDto.getType())
                    .rate(taxDto.getValue())
                    .deleted(taxDto.getDeleted());
        }
}
