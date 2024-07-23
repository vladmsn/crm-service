package com.mmdevelopement.crm.domain.partner.controller;

import com.mmdevelopement.crm.domain.partner.entity.dto.PartnerDto;
import com.mmdevelopement.crm.domain.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<PartnerDto> getPartners() {
        return partnerService.findPartners();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PartnerDto getPartnerById(@PathVariable Integer id) {
        return partnerService.getPartnerById(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public PartnerDto createPartner(@RequestBody PartnerDto partnerDto) {
        return partnerService.savePartner(partnerDto);
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public PartnerDto updatePartner(@RequestBody PartnerDto partnerDto) {
        return partnerService.savePartner(partnerDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePartner(@PathVariable Integer id) {
        partnerService.deletePartner(id);
    }

}
