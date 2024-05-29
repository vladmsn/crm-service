package com.mmdevelopement.crm.domain.partner.service;

import com.mmdevelopement.crm.domain.partner.entity.PartnerEntity;
import com.mmdevelopement.crm.domain.partner.entity.dto.PartnerDto;
import com.mmdevelopement.crm.domain.partner.repository.PartnerRepository;
import com.mmdevelopement.crm.infrastructure.exceptions.HttpStatusException;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import com.mmdevelopement.crm.utils.ImageUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public List<PartnerDto> findPartners() {
        return partnerRepository.findAll().stream()
                .map(PartnerDto::fromEntity)
                .map(partnerDto -> partnerDto.image(partnerDto.image() != null ? "present, but omitted" : null))
                .toList();
    }

    public PartnerDto getPartnerById(Integer id) {
        return partnerRepository.findById(id)
                .map(PartnerDto::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Partner with id " + id + " not found"));
    }

    @Transactional
    public PartnerDto savePartner(PartnerDto partnerDto) {

        try {
            PartnerEntity partnerEntity = partnerDto.toEntity();
            partnerRepository.save(partnerEntity);
            return PartnerDto.fromEntity(partnerEntity);
        } catch (Exception e) {
            log.error("Error saving partner", e);
            throw new HttpStatusException("Error saving partner");
        }
    }

    public void deletePartner(Integer id) {
        partnerRepository.deleteById(id);
    }
}
