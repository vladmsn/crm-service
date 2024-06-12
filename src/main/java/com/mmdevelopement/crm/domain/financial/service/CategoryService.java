package com.mmdevelopement.crm.domain.financial.service;

import com.mmdevelopement.crm.domain.financial.entity.CategoryEntity;
import com.mmdevelopement.crm.domain.financial.entity.dto.CategoryDto;
import com.mmdevelopement.crm.domain.financial.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        log.debug("Getting all categories");

        return categoryRepository.findAll()
                .stream()
                .map(CategoryDto::toDto)
                .toList();
    }

    public CategoryDto getCategoryById(Integer id) {
        log.debug("Getting category by id: {}", id);

        return categoryRepository.findById(id)
                .map(CategoryDto::toDto)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public CategoryDto saveCategory(CategoryDto categoryDto) {
        CategoryEntity categoryEntity =  categoryRepository.save(categoryDto.toEntity());

        log.info("Category saved: {}", categoryEntity);

        return CategoryDto.toDto(categoryEntity);
    }

    public void deleteCategory(Integer id) {
        log.debug("Deleting category by id: {}", id);

        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(categoryEntity);
    }
}
