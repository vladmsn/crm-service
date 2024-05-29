package com.mmdevelopement.crm.domain.financial.service;


import com.mmdevelopement.crm.domain.financial.entity.dto.InvoiceDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.InvoiceElementDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.InvoicePaymentDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.TaxDto;
import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoiceElementEntity;
import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoiceEntity;
import com.mmdevelopement.crm.domain.financial.repository.InvoicePaymentRepository;
import com.mmdevelopement.crm.domain.financial.repository.InvoiceRepository;
import com.mmdevelopement.crm.domain.partner.service.PartnerService;
import com.mmdevelopement.crm.infrastructure.exceptions.BadRequestException;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final InvoicePaymentRepository invoicePaymentRepository;

    private final TaxService taxService;

    private final ElementService elementService;

    private final CategoryService categoryService;

    private final PartnerService partnerService;

    public InvoiceDto getInvoiceById(Integer id) {
        log.debug("Getting invoice by id: {}", id);

        return invoiceRepository.findById(id)
                .map(InvoiceDto::toDto)
                .map(invoiceDto -> {
                    decorateWithDetails(invoiceDto);
                    return invoiceDto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }

    public List<InvoiceDto> getAllInvoices() {
        log.debug("Getting all invoices");

        return invoiceRepository.findAll()
                .stream()
                .map(InvoiceDto::toDto)
                .toList();
    }

    private void decorateWithDetails(InvoiceDto invoiceDto) {

        var partner = partnerService.getPartnerById(invoiceDto.partnerId());
        var category = categoryService.getCategoryById(invoiceDto.categoryId());

        invoiceDto.partnerName(partner.name());
        invoiceDto.categoryName(category.name());
    }

    public List<InvoiceElementDto> getInvoiceElements(Integer id) {

        if (id == null) {
            throw new BadRequestException("Invoice id is required");
        }

        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        List<InvoiceElementEntity> invoiceElements = invoice.elements();

        return invoiceElements.stream()
                .map(invElement -> {
                    var element = elementService.getElementById(invElement.elementId());
                    var tax = taxService.getTaxById(invElement.taxId());

                    return InvoiceElementDto.fromElement(element, invElement.quantity(), invoice.direction(), tax.value());
                })
                .toList();
    }

    public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
        InvoiceEntity savedInvoice = invoiceRepository.save(InvoiceDto.toEntity(invoiceDto));

        log.info("Created invoice: {}", savedInvoice);

        return InvoiceDto.toDto(savedInvoice);
    }

    @Transactional
    public List<InvoiceElementDto> addInvoiceElements(Integer id, Map<InvoiceElementDto, TaxDto> invoiceElementMap) {

        if (id == null) {
            throw new BadRequestException("Invoice id is required");
        }

        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        List<InvoiceElementEntity> invoiceElements = invoiceElementMap.entrySet()
                .stream()
                .map(entry -> {
                    var element = elementService.getElementById(entry.getKey().elementId());
                    var tax = taxService.getTaxById(entry.getValue().id());

                    return new InvoiceElementEntity()
                            .elementId(element.id())
                            .quantity(entry.getKey().quantity())
                            .taxId(tax.id());
                })
                .toList();

        invoice.elements(invoiceElements);

        invoiceRepository.save(invoice);
        log.info("Added elements to invoice: {}", invoice);

        return invoiceElements.stream()
                .map(invElement -> {
                    var element = elementService.getElementById(invElement.elementId());
                    var tax = taxService.getTaxById(invElement.taxId());

                    return InvoiceElementDto.fromElement(element, invElement.quantity(), invoice.direction(), tax.value());
                })
                .toList();
    }

    public List<InvoicePaymentDto> getInvoicePayments(Integer id) {

        if (id == null) {
            throw new BadRequestException("Invoice id is required");
        }

        log.debug("Getting payments for invoice with id: {}", id);

        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return invoicePaymentRepository.findAllByInvoiceId(id).stream()
                .map(invoicePayment -> {
                    var partner = partnerService.getPartnerById(invoicePayment.partnerId());
                    return InvoicePaymentDto.fromEntity(invoicePayment, partner.name(), partner.CUI(), invoice.direction());
                })
                .toList();
    }

    public InvoicePaymentDto addInvoicePayment(Integer id, InvoicePaymentDto invoicePaymentDto) {

        if (id == null) {
            throw new BadRequestException("Invoice id is required");
        }

        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        invoicePaymentDto.invoiceId(id);
        invoicePaymentDto.partnerId(invoice.partnerId());

        var savedPayment = invoicePaymentRepository.save(invoicePaymentDto.toEntity());

        log.info("Added payment to invoice: {}", savedPayment);

        var partner = partnerService.getPartnerById(savedPayment.partnerId());
        return InvoicePaymentDto.fromEntity(savedPayment, partner.name(), partner.CUI(), invoice.direction());
    }
}
