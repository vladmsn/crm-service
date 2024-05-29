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
                .map(TaxDto::toDto)
                .toList();
    }

    public TaxDto getTaxById(Integer id) {
        log.debug("Getting tax by id: {}", id);

        return taxRepository.findById(id)
                .map(TaxDto::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Tax not found"));
    }

    public TaxDto saveTax(TaxDto taxDto) {
        TaxEntity taxEntity = taxRepository.save(TaxDto.toEntity(taxDto));

        log.info("Tax saved: {}", taxEntity);

        return TaxDto.toDto(taxEntity);
    }

    public void deleteTax(Integer id) {
        log.debug("Deleting tax by id: {}", id);

        TaxEntity taxEntity = taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax not found"));

        taxEntity.deleted(true);
        taxRepository.save(taxEntity);
    }
}
