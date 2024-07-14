package com.mmdevelopement.crm.domain.financial.entity.dto;

import com.mmdevelopement.crm.domain.financial.entity.CategoryEntity;
import com.mmdevelopement.crm.domain.financial.entity.enums.CategoryType;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class CategoryDto {

    private Integer id;

    private String name;

    private String type;

    private String colorCode;


    public static CategoryDto toDto(CategoryEntity categoryEntity) {
        return new CategoryDto()
                .setId(categoryEntity.id())
                .setName(categoryEntity.name())
                .setType(categoryEntity.type())
                .setColorCode(categoryEntity.colorCode());
    }

    public CategoryEntity toEntity() {
        return new CategoryEntity()
                .id(getId())
                .name(getName())
                .type(getType())
                .colorCode(getColorCode());
    }

}
