package com.mmdevelopement.crm.domain.financial.service;


import com.mmdevelopement.crm.domain.financial.entity.ElementEntity;
import com.mmdevelopement.crm.domain.financial.entity.dto.ElementDto;
import com.mmdevelopement.crm.domain.financial.repository.CategoryRepository;
import com.mmdevelopement.crm.domain.financial.repository.ElementRepository;
import com.mmdevelopement.crm.domain.financial.repository.TaxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ElementService {

    private final ElementRepository elementRepository;

    private final TaxRepository taxRepository;

    private final CategoryRepository categoryRepository;

    public List<ElementDto> getAllElements() {
        log.debug("Getting all elements");

        return elementRepository.findAll()
                .stream()
                .map(ElementDto::toDto)
                .peek(elementDto -> decorateWithDetails(elementDto))
                .toList();
    }

    public ElementDto getElementById(Integer id) {
        log.debug("Getting element by id: {}", id);

        return elementRepository.findById(id)
                .map(ElementDto::toDto)
                .map(elementDto -> {
                    decorateWithDetails(elementDto);
                    return elementDto;
                })
                .orElseThrow(() -> new RuntimeException("Element not found"));
    }

    public ElementDto saveElement(ElementDto elementDto) {
        ElementEntity elementEntity =  elementRepository.save(ElementDto.toEntity(elementDto));

        log.info("Element saved: {}", elementEntity);

        ElementDto element = ElementDto.toDto(elementEntity);
        decorateWithDetails(element);

        return element;
    }

    public void deleteElement(Integer id) {
        log.debug("Deleting element by id: {}", id);

        ElementEntity elementEntity = elementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Element not found"));

        elementEntity.deleted(true);
        elementRepository.save(elementEntity);
    }

    private void decorateWithDetails(ElementDto elementDto) {
        elementDto.categoryName(
                categoryRepository.findById(elementDto.categoryId())
                        .map(categoryEntity -> categoryEntity.name())
                        .orElse(null));
        elementDto.taxValue(taxRepository.findById(elementDto.taxId())
                .map(taxEntity -> taxEntity.rate()).orElse(null));
    }
}
