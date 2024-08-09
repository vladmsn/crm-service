package com.mmdevelopement.crm.domain.financial.service;


import com.mmdevelopement.crm.domain.financial.entity.ElementEntity;
import com.mmdevelopement.crm.domain.financial.entity.dto.ElementDto;
import com.mmdevelopement.crm.domain.financial.repository.CategoryRepository;
import com.mmdevelopement.crm.domain.financial.repository.ElementRepository;
import com.mmdevelopement.crm.domain.financial.repository.TaxRepository;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
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
                .filter(elementEntity -> !elementEntity.deleted())
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
                .orElseThrow(() -> new ResourceNotFoundException("Element not found"));
    }

    public ElementDto createElement(ElementDto elementDto) {
        log.debug("Creating element: {}", elementDto);
        ElementEntity elementEntity =  elementDto.toEntity();
        elementEntity.deleted(false);

        elementEntity = elementRepository.save(elementEntity);
        log.info("Element saved: {}", elementEntity);

        ElementDto element = ElementDto.toDto(elementEntity);
        decorateWithDetails(element);

        return element;
    }

    public ElementDto updateElement(ElementDto elementDto) {
        log.debug("Updating element: {}", elementDto);
        if (elementDto.getId() == null) {
            throw new IllegalArgumentException("Element id is required when updating");
        }

        ElementEntity elementEntity = elementRepository.findById(elementDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Element with id " + elementDto.getId() + " not found"));

        elementEntity = elementEntity.name(elementDto.getName())
                .categoryId(elementDto.getCategoryId())
                .taxId(elementDto.getTaxId())
                .sellingPrice(elementDto.getSellingPrice())
                .acquisitionPrice(elementDto.getAcquisitionPrice())
                .description(elementDto.getDescription())
                .type(elementDto.getType());

        elementEntity = elementRepository.save(elementEntity);

        log.info("Element updated: {}", elementEntity);

        ElementDto element = ElementDto.toDto(elementEntity);
        decorateWithDetails(element);

        return element;
    }

    public void deleteElement(Integer id) {
        log.debug("Deleting element by id: {}", id);

        ElementEntity elementEntity = elementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Element not found"));

        elementEntity.deleted(true);
        elementRepository.save(elementEntity);

        log.info("Element with id {} marked as deleted", id);
    }

    private void decorateWithDetails(ElementDto elementDto) {
        elementDto.setCategoryName(
                categoryRepository.findById(elementDto.getCategoryId())
                        .map(categoryEntity -> categoryEntity.name())
                        .orElse(null));
        elementDto.setTaxValue(taxRepository.findById(elementDto.getTaxId())
                .map(taxEntity -> taxEntity.rate()).orElse(null));
    }
}
