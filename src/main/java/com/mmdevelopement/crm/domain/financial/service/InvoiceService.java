package com.mmdevelopement.crm.domain.financial.service;


import com.mmdevelopement.crm.domain.financial.entity.dto.InvoiceDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.InvoiceElementDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.InvoicePaymentDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.TaxDto;
import com.mmdevelopement.crm.domain.financial.entity.enums.InvoiceDirection;
import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoiceElementEntity;
import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoiceEntity;
import com.mmdevelopement.crm.domain.financial.entity.invoices.QInvoiceEntity;
import com.mmdevelopement.crm.domain.financial.repository.InvoicePaymentRepository;
import com.mmdevelopement.crm.domain.financial.repository.InvoiceRepository;
import com.mmdevelopement.crm.domain.partner.service.PartnerService;
import com.mmdevelopement.crm.infrastructure.exceptions.BadRequestException;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import com.querydsl.core.types.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;


@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final InvoicePaymentRepository invoicePaymentRepository;

    private final ElementService elementService;

    private final CategoryService categoryService;

    private final PartnerService partnerService;

    private final BankAccountService bankAccountService;


    // ==============================  Invoices ==============================

    public List<InvoiceDto> getAllInvoices() {
        log.debug("Getting all invoices");

        return invoiceRepository.findAll()
                .stream()
                .map(invoice -> {
                    var invoiceDto = InvoiceDto.toDto(invoice);
                    decorateWithDetails(invoiceDto);
                    return invoiceDto;
                })
                .toList();
    }


    public List<InvoiceDto> findInvoices(Predicate predicate) {
        log.debug("Finding invoices");

        return StreamSupport.stream(invoiceRepository.findAll(predicate).spliterator(), false)
                .map(invoice -> {
                    var invoiceDto = InvoiceDto.toDto(invoice);
                    decorateWithDetails(invoiceDto);
                    return invoiceDto;
                })
                .toList();
    }

    public InvoiceDto getInvoiceById(Integer id) {
        log.debug("Getting invoice by id: {}", id);

        return invoiceRepository.findById(id)
                .map(InvoiceDto::toDto)
                .map(invoiceDto -> {
                    decorateWithDetails(invoiceDto);
                    invoiceDto.setElements(getInvoiceElements(id));
                    return invoiceDto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }

    @Transactional
    public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
        InvoiceEntity invoice = invoiceDto.toEntity();
        invoice.remainingAmount(invoice.total());

        if (invoice.elements().isEmpty()) {
            throw new BadRequestException("Invoice must have at least one element");
        }

        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);
        addInvoiceElements(savedInvoice.id(), invoiceDto.getElements());

        log.info("Created invoice: {}", savedInvoice);

        return InvoiceDto.toDto(savedInvoice);
    }

    // ================================== Invoice Elements ==================================

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

                    return InvoiceElementDto.fromElement(element, invElement.quantity(), invElement.price(), invElement.tax());
                })
                .toList();
    }

    @Transactional
    public void addInvoiceElements(Integer id, List<InvoiceElementDto> invoiceElementsList) {

        if (id == null) {
            throw new BadRequestException("Invoice id is required");
        }

        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        List<InvoiceElementEntity> invoiceElements = invoiceElementsList
                .stream()
                .map(invElement -> {
                    // look up element
                    elementService.getElementById(invElement.getElementId());

                    return new InvoiceElementEntity()
                            .elementId(invElement.getElementId())
                            .quantity(invElement.getQuantity())
                            .tax(invElement.getElementTax())
                            .price(invElement.getElementPrice());
                })
                .toList();

        invoice.elements(invoiceElements);

        invoiceRepository.save(invoice);
        log.info("Added elements to invoice: {}", invoice);
    }

    // ================== Invoice Payments ==================

    public List<InvoicePaymentDto> getAllInvoicePayments() {
        log.debug("Getting all invoice payments");

        return invoicePaymentRepository.findAll()
                .stream()
                .map(invoicePayment -> {
                    var partner = partnerService.getPartnerById(invoicePayment.partnerId());
                    var invoice = invoiceRepository.findById(invoicePayment.invoiceId())
                            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

                    return InvoicePaymentDto.fromEntity(invoicePayment, partner.getName(), partner.getCUI(), invoice.direction());
                })
                .toList();
    }

    public List<InvoicePaymentDto> getInvoicePayments(Integer id) {

        if (id == null) {
            throw new BadRequestException("Invoice identifier is required");
        }

        log.debug("Getting payments for invoice with identifier: {}", id);

        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return invoicePaymentRepository.findAllByInvoiceId(id)

                .stream()
                .map(invoicePayment -> {
                    var partner = partnerService.getPartnerById(invoicePayment.partnerId());
                    return InvoicePaymentDto.fromEntity(invoicePayment, partner.getName(), partner.getCUI(), invoice.direction());
                })
                .toList();
    }

    @Transactional
    public InvoicePaymentDto addInvoicePayment(InvoicePaymentDto invoicePaymentDto) {

        if (invoicePaymentDto.getInvoiceId() == null && invoicePaymentDto.getReference() == null
                                                        || invoicePaymentDto.getBankAccountId() == null) {
            throw new BadRequestException("Invoice id or reference is required");
        }

        // look-up checks
        if (invoicePaymentDto.getInvoiceId() != null) {
            getInvoiceById(invoicePaymentDto.getInvoiceId());
        }
        partnerService.getPartnerById(invoicePaymentDto.getPartnerId());

        var savedPayment = invoicePaymentRepository.save(invoicePaymentDto.toEntity());
        log.info("Added payment to invoice: {}", savedPayment);

        bankAccountService.executeTransaction(invoicePaymentDto.getBankAccountId(), savedPayment.amount(), invoicePaymentDto.getDirection());

        var partner = partnerService.getPartnerById(savedPayment.partnerId());
        return InvoicePaymentDto.fromEntity(savedPayment, partner.getName(), partner.getCUI(), invoicePaymentDto.getDirection());
    }

    // ================== Private methods ==================

    private void decorateWithDetails(InvoiceDto invoiceDto) {

        var partner = partnerService.getPartnerById(invoiceDto.getPartnerId());
        var category = categoryService.getCategoryById(invoiceDto.getCategoryId());

        invoiceDto.setPartnerName(partner.getName());
        invoiceDto.setCategoryName(category.getName());
    }

}
