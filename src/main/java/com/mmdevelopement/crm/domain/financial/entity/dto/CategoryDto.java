package com.mmdevelopement.crm.domain.financial.entity.dto;

import com.mmdevelopement.crm.domain.financial.entity.CategoryEntity;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true, fluent = true)
public class CategoryDto {

    private Integer id;

    private String name;

    private String type;

    private String colorCode;


    public static CategoryDto toDto(CategoryEntity categoryEntity) {
        return new CategoryDto()
                .id(categoryEntity.id())
                .name(categoryEntity.name())
                .type(categoryEntity.type())
                .colorCode(categoryEntity.colorCode());
    }

    public static CategoryEntity toEntity(CategoryDto categoryDto) {
        return new CategoryEntity()
                .id(categoryDto.id())
                .name(categoryDto.name())
                .type(categoryDto.type())
                .colorCode(categoryDto.colorCode());
    }

}
