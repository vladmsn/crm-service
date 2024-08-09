package com.mmdevelopement.crm.domain.partner.service;

import com.mmdevelopement.crm.domain.partner.entity.PartnerEntity;
import com.mmdevelopement.crm.domain.partner.entity.dto.PartnerDto;
import com.mmdevelopement.crm.domain.partner.repository.PartnerRepository;
import com.mmdevelopement.crm.infrastructure.exceptions.BadRequestException;
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
        log.info("Finding partners");

        return partnerRepository.findAll().stream()
                .filter(partnerEntity -> !partnerEntity.deleted())
                .map(PartnerDto::fromEntity)
                .map(partnerDto -> partnerDto.setImage(partnerDto.getImage() != null ? "present, but omitted" : null))
                .toList();
    }

    public PartnerDto getPartnerById(Integer id) {
        log.info("Getting partner by id {}", id);

        return partnerRepository.findById(id)
                .map(PartnerDto::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Partner with id " + id + " not found"));
    }

    @Transactional
    public PartnerDto updatePartner(PartnerDto partnerDto) {
        if (partnerDto.getId() == null) {
            throw new BadRequestException("Partner id is required when updating");
        }

        if (partnerRepository.existsById(partnerDto.getId())) {
            throw new BadRequestException("Partner with id " + partnerDto.getId() + " not found");
        }

        log.info("Saving partner {}", partnerDto.toString());

        try {
            PartnerEntity partnerEntity = partnerDto.toEntity();
            partnerRepository.save(partnerEntity);
            return PartnerDto.fromEntity(partnerEntity);
        } catch (Exception e) {
            log.error("Error saving partner", e);
            throw new HttpStatusException("Error saving partner");
        }
    }

    @Transactional
    public PartnerDto createPartner(PartnerDto partnerDto) {
        log.info("Creating partner {}", partnerDto);

        try {
            PartnerEntity partnerEntity = partnerDto.toEntity();
            partnerEntity.deleted(false);
            partnerRepository.save(partnerEntity);
            return PartnerDto.fromEntity(partnerEntity);
        } catch (Exception e) {
            log.error("Error creating partner", e);
            throw new HttpStatusException("Error creating partner");
        }
    }

    public void deletePartner(Integer id) {
        log.info("Deleting partner with id {}", id);

        PartnerEntity partnerEntity = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner with id " + id + " not found"));

        partnerEntity.deleted(true);
        partnerRepository.save(partnerEntity);
        log.info("Partner with id {} marked as deleted", id);
    }
}
