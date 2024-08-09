package com.mmdevelopement.crm.domain.financial.controller;

import com.mmdevelopement.crm.domain.financial.entity.dto.TaxDto;
import com.mmdevelopement.crm.domain.financial.service.TaxService;
import com.mmdevelopement.crm.infrastructure.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/financial/tax")
@RequiredArgsConstructor
public class TaxController {

    private final TaxService taxService;

    @GetMapping("/")
    public List<TaxDto> getAllTaxes() {
        return taxService.getAllTaxes();
    }

    @GetMapping("/{id}")
    public TaxDto getTaxById(@PathVariable Integer id) {
        return taxService.getTaxById(id);
    }

    @PostMapping("/")
    public TaxDto saveTax(@RequestBody TaxDto taxDto) {
        return taxService.createTax(taxDto);
    }

    @PutMapping("/")
    public TaxDto updateTax(@RequestBody TaxDto taxDto) {
        return taxService.updateTax(taxDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTax(@PathVariable Integer id) {
        taxService.deleteTax(id);
    }
}
