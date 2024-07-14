package com.mmdevelopement.crm.domain.financial.controller;

import com.mmdevelopement.crm.domain.financial.entity.dto.InvoiceDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.InvoiceElementDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.InvoicePaymentDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.TaxDto;
import com.mmdevelopement.crm.domain.financial.entity.invoices.QInvoiceEntity;
import com.mmdevelopement.crm.domain.financial.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/financial/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/")
    public List<InvoiceDto> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public InvoiceDto getInvoiceById(@PathVariable Integer id) {
        return invoiceService.getInvoiceById(id);
    }

    @GetMapping("/next-number")
    public Integer getNextInvoiceNumber() {
        return invoiceService.getNextInvoiceNumber();
    }

    @PostMapping("/")
    public InvoiceDto createInvoice(@RequestBody InvoiceDto invoiceDto) {
        return invoiceService.createInvoice(invoiceDto);
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Integer id) {
        invoiceService.deleteInvoice(id);
    }

    @GetMapping("/payments")
    public List<InvoicePaymentDto> getAllInvoicePayments() {
        return invoiceService.getAllInvoicePayments();
    }

    @GetMapping("/{identifier}/payments")
    public List<InvoicePaymentDto> getInvoicePayments(@PathVariable Integer identifier) {
        return invoiceService.getInvoicePayments(identifier);
    }

    @PostMapping("/payments")
    public InvoicePaymentDto addInvoicePayment(@RequestBody InvoicePaymentDto invoicePaymentDto) {
        return invoiceService.addInvoicePayment(invoicePaymentDto);
    }
}
