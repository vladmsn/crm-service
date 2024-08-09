package com.mmdevelopement.crm.domain.financial.service;


import com.mmdevelopement.crm.domain.financial.entity.TaxEntity;
import com.mmdevelopement.crm.domain.financial.entity.dto.TaxDto;
import com.mmdevelopement.crm.domain.financial.repository.TaxRepository;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class TaxService {

    private final TaxRepository taxRepository;

    public List<TaxDto> getAllTaxes() {
        log.debug("Getting all taxes");

        return taxRepository.findAll()
                .stream()
                .filter(taxEntity -> !taxEntity.deleted())
                .map(TaxDto::toDto)
                .toList();
    }

    public TaxDto getTaxById(Integer id) {
        log.debug("Getting tax by id: {}", id);

        return taxRepository.findById(id)
                .map(TaxDto::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Tax not found"));
    }

    public TaxDto createTax(TaxDto taxDto) {
        log.debug("Creating tax: {}", taxDto);
        TaxEntity taxEntity = TaxDto.toEntity(taxDto);
        taxEntity.deleted(false);

        taxEntity = taxRepository.save(taxEntity);

        log.info("Tax saved: {}", taxEntity);

        return TaxDto.toDto(taxEntity);
    }

    public TaxDto updateTax(TaxDto taxDto) {
        log.debug("Updating tax: {}", taxDto);
        if (taxDto.getId() == null) {
            throw new IllegalArgumentException("Tax id is required when updating");
        }

        TaxEntity taxEntity = taxRepository.findById(taxDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tax with id " + taxDto.getId() + " not found"));

        taxEntity = taxEntity.name(taxDto.getName())
                .rate(taxDto.getValue())
                .type(taxDto.getType());

        taxEntity = taxRepository.save(taxEntity);

        log.info("Tax updated: {}", taxEntity);
        return TaxDto.toDto(taxEntity);
    }

    public void deleteTax(Integer id) {
        log.debug("Deleting tax by id: {}", id);

        TaxEntity taxEntity = taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax not found"));

        taxEntity.deleted(true);
        taxRepository.save(taxEntity);

        log.info("Tax with id {} marked as deleted", id);
    }
}
