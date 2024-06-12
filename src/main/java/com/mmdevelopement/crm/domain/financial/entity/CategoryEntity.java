package com.mmdevelopement.crm.domain.financial.entity;


import com.mmdevelopement.crm.domain.financial.entity.enums.CategoryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
@Entity
@Table(name = "category")
public class CategoryEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "name")
        private String name;

        @Column(name = "type")
        private String type;

        @Column(name = "color_code")
        private String colorCode;

        @Column(name = "parent_category_id")
        private Integer parentCategoryId;
}
