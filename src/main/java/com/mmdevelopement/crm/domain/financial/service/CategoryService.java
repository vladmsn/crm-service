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

    public List<CategoryDto> getAllCategories(boolean includeDeleted) {
        log.debug("Getting all categories");

        return categoryRepository.findAll()
                .stream()
                .filter(categoryEntity -> includeDeleted || !categoryEntity.deleted())
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
        log.debug("Saving category: {}", categoryDto);

        CategoryEntity categoryEntity = categoryDto.toEntity();
        categoryEntity.deleted(false);

        categoryEntity = categoryRepository.save(categoryEntity);

        log.info("Category saved: {}", categoryEntity);

        return CategoryDto.toDto(categoryEntity);
    }

    public void deleteCategory(Integer id) {
        log.debug("Deleting category by id: {}", id);

        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryEntity.deleted(true);
        categoryRepository.save(categoryEntity);

        log.info("Category with id {} marked as deleted", id);
    }
}
