package com.mmdevelopement.crm.domain.financial.controller;

import com.mmdevelopement.crm.domain.financial.entity.dto.ElementDto;
import com.mmdevelopement.crm.domain.financial.service.ElementService;
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
@RequestMapping("/api/v1/financial/element")
@RequiredArgsConstructor
public class ElementController {

    private final ElementService elementService;

    @GetMapping("/")
    public List<ElementDto> getAllElements() {
        return elementService.getAllElements();
    }

    @GetMapping("/{id}")
    public ElementDto getElementById(@PathVariable Integer id) {
        return elementService.getElementById(id);
    }

    @PostMapping("/")
    public ElementDto saveElement(@RequestBody ElementDto elementDto) {
        return elementService.saveElement(elementDto);
    }

    @PutMapping("/")
    public ElementDto updateElement(@RequestBody ElementDto elementDto) {
        if (elementDto.id() == null) {
            throw new BadRequestException("Element id is required for update");
        }

        return elementService.saveElement(elementDto);
    }

    @DeleteMapping("/{id}")
    public void deleteElement(@PathVariable Integer id) {
        elementService.deleteElement(id);
    }

}
