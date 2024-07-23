package com.mmdevelopement.crm.domain.financial.service;


import com.mmdevelopement.crm.domain.financial.entity.dto.InvoiceDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.InvoiceElementDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.InvoicePaymentDto;
import com.mmdevelopement.crm.domain.financial.entity.enums.InvoiceDirection;
import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoiceElementEntity;
import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoiceEntity;
import com.mmdevelopement.crm.domain.financial.entity.invoices.QInvoiceEntity;
import com.mmdevelopement.crm.domain.financial.repository.InvoicePaymentRepository;
import com.mmdevelopement.crm.domain.financial.repository.InvoiceRepository;
import com.mmdevelopement.crm.domain.organization.service.OrganizationService;
import com.mmdevelopement.crm.domain.partner.service.PartnerService;
import com.mmdevelopement.crm.infrastructure.exceptions.BadRequestException;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    private final OrganizationService organizationService;

    // ==============================  Invoices ==============================

    public List<InvoiceDto> getAllInvoices() {
        log.info("Getting all invoices");

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
        log.info("Finding invoices");

        return StreamSupport.stream(invoiceRepository.findAll(predicate).spliterator(), false)
                .map(invoice -> {
                    var invoiceDto = InvoiceDto.toDto(invoice);
                    decorateWithDetails(invoiceDto);
                    return invoiceDto;
                })
                .toList();
    }

    public InvoiceDto getInvoiceById(Integer id) {
        log.info("Getting invoice by id: {}", id);

        return invoiceRepository.findById(id)
                .map(InvoiceDto::toDto)
                .map(invoiceDto -> {
                    decorateWithDetails(invoiceDto);
                    invoiceDto.setElements(getInvoiceElements(id));
                    return invoiceDto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }

    public Integer getNextInvoiceNumber() {
        log.info("Getting next invoice number");

        var invoiceInfo = organizationService.getCurrentOrganizationInvoiceInfo();

        Optional<InvoiceEntity> latestInvoice = invoiceRepository.findFirstByDirectionOrderByIdDesc(InvoiceDirection.OUT.name());

        if (latestInvoice.isPresent()) {
            String invoiceNumber = latestInvoice.get().invoiceNumber();
            String prefix = invoiceInfo.prefix();
            // Assuming the invoice number format includes a prefix followed by a numeric value
            if (invoiceNumber.startsWith(prefix)) {
                String numericPart = invoiceNumber.substring(prefix.length());
                try {
                    int number = Integer.parseInt(numericPart);
                    return number + 1; // Increment the last invoice number by 1
                } catch (NumberFormatException e) {
                    log.error("Failed to parse the numeric part of the invoice number: {}", numericPart, e);
                }
            }
        }

        return invoiceInfo.startingNumber();
    }

    @Transactional
    public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
        log.info("Creating invoice: {}", invoiceDto);

        InvoiceEntity invoice = invoiceDto.toEntity();
        invoice.remainingAmount(invoice.total());

        if (invoiceDto.getElements().isEmpty()) {
            throw new BadRequestException("Invoice must have at least one element");
        }

        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);
        addInvoiceElements(savedInvoice, invoiceDto.getElements());

        log.info("Created invoice: {}", savedInvoice);

        return InvoiceDto.toDto(savedInvoice);
    }

    @Transactional
    public void deleteInvoice(Integer id) {
        log.info("Deleting invoice with id: {}", id);

        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if (!Objects.equals(invoice.remainingAmount(), invoice.total())) {
            invoicePaymentRepository.findAllByInvoiceId(id)
                    .forEach(invoicePayment -> {
                        bankAccountService.rollbackTransaction(invoicePayment.bankAccountId(), invoicePayment.amount(), invoicePayment.paymentDirection());
                        invoicePaymentRepository.delete(invoicePayment);
                    });
        }

        invoiceRepository.delete(invoice);
        log.info("Deleted invoice: {}", invoice);
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
    public void addInvoiceElements(InvoiceEntity invoice, List<InvoiceElementDto> invoiceElementsList) {
        log.info("Adding elements to invoice: {}", invoice);
        List<InvoiceElementEntity> invoiceElements = invoiceElementsList
                .stream()
                .map(invElement -> {
                    // look up element
                    elementService.getElementById(invElement.getElementId());

                    return new InvoiceElementEntity()
                            .invoiceId(invoice.id())
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

    public List<InvoicePaymentDto> findPayments(Predicate predicate) {
        log.info("Finding invoice payments");

        return StreamSupport.stream(invoicePaymentRepository.findAll(predicate).spliterator(), false)
                .map(invoicePayment -> {
                    var partner = partnerService.getPartnerById(invoicePayment.partnerId());
                    var category = categoryService.getCategoryById(invoicePayment.categoryId());
                    return InvoicePaymentDto.fromEntity(invoicePayment, partner.getName(), partner.getCui(), category.getName(),
                            InvoiceDirection.fromString(invoicePayment.paymentDirection()));
                })
                .toList();
    }

    public List<InvoicePaymentDto> getAllInvoicePayments() {
        log.info("Getting all invoice payments");

        return invoicePaymentRepository.findAll()
                .stream()
                .map(invoicePayment -> {
                    var partner = partnerService.getPartnerById(invoicePayment.partnerId());
                    var category = categoryService.getCategoryById(invoicePayment.categoryId());
                    return InvoicePaymentDto.fromEntity(invoicePayment, partner.getName(), partner.getCui(), category.getName(),
                            InvoiceDirection.fromString(invoicePayment.paymentDirection()));
                })
                .toList();
    }

    public List<InvoicePaymentDto> getInvoicePayments(Integer id) {

        if (id == null) {
            throw new BadRequestException("Invoice identifier is required");
        }

        log.info("Getting payments for invoice with identifier: {}", id);

        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return invoicePaymentRepository.findAllByInvoiceId(id)

                .stream()
                .map(invoicePayment -> {
                    var partner = partnerService.getPartnerById(invoicePayment.partnerId());
                    var category = categoryService.getCategoryById(invoicePayment.categoryId());

                    return InvoicePaymentDto.fromEntity(invoicePayment, partner.getName(), partner.getCui(), category.getName(),
                            InvoiceDirection.valueOf(invoice.direction()));
                })
                .toList();
    }

    @Transactional
    public InvoicePaymentDto addInvoicePayment(InvoicePaymentDto invoicePaymentDto) {

        if (invoicePaymentDto.getInvoiceId() == null && invoicePaymentDto.getReference() == null
                                                        || invoicePaymentDto.getBankAccountId() == null) {
            throw new BadRequestException("Invoice id or reference, bank account id are required");
        }

        partnerService.getPartnerById(invoicePaymentDto.getPartnerId());

        var savedPayment = invoicePaymentRepository.save(invoicePaymentDto.toEntity());
        log.info("Added payment to invoice: {}", savedPayment);

        bankAccountService.executeTransaction(invoicePaymentDto.getBankAccountId(), savedPayment.amount(), invoicePaymentDto.getDirection());
        log.info("Executed transaction for payment: {}", savedPayment);

        if (invoicePaymentDto.getInvoiceId() != null) {
            InvoiceEntity invoice = invoiceRepository.findById(invoicePaymentDto.getInvoiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

            invoice.remainingAmount(invoice.remainingAmount() - savedPayment.amount());
            invoiceRepository.save(invoice);
        }

        var partner = partnerService.getPartnerById(savedPayment.partnerId());
        var category = categoryService.getCategoryById(invoicePaymentDto.getCategoryId());

        return InvoicePaymentDto.fromEntity(savedPayment, partner.getName(), partner.getCui(), category.getName(),
                invoicePaymentDto.getDirection());
    }

    // ================== Private methods ==================

    private void decorateWithDetails(InvoiceDto invoiceDto) {

        var partner = partnerService.getPartnerById(invoiceDto.getPartnerId());
        var category = categoryService.getCategoryById(invoiceDto.getCategoryId());

        invoiceDto.setPartnerName(partner.getName());
        invoiceDto.setCategoryName(category.getName());
    }
}
