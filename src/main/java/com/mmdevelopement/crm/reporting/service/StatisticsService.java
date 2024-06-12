package com.mmdevelopement.crm.reporting.service;


import com.mmdevelopement.crm.domain.financial.entity.dto.InvoiceDto;
import com.mmdevelopement.crm.domain.financial.entity.enums.InvoiceDirection;
import com.mmdevelopement.crm.domain.financial.entity.invoices.QInvoiceEntity;
import com.mmdevelopement.crm.domain.financial.service.BankAccountService;
import com.mmdevelopement.crm.domain.financial.service.CategoryService;
import com.mmdevelopement.crm.domain.financial.service.InvoiceService;
import com.mmdevelopement.crm.reporting.entity.AggregatedStatistics;
import com.mmdevelopement.crm.reporting.entity.items.AccountSoldItem;
import com.mmdevelopement.crm.reporting.entity.items.Payables;
import com.mmdevelopement.crm.reporting.entity.items.Receivables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final CategoryService categoryService;
    private final BankAccountService bankAccountService;
    private final InvoiceService invoiceService;

    public AggregatedStatistics getDashboardData(Date fromDate, Date toDate) {
        log.info("Getting dashboard data");
        return AggregatedStatistics.builder()
                .accountsSold(getAccountsSold())
                .receivables(getReceivables(fromDate, toDate))
                .payables(getPayables(fromDate, toDate))
                .cashFlow(null)
                .incomeByCategory(null)
                .expensesByCategory(null)
                .build();
    }


    public List<AccountSoldItem> getAccountsSold() {
        return bankAccountService.getAllAccounts()
                .stream()
                .map(bankAccountDto ->  AccountSoldItem.builder()
                            .accountName(bankAccountDto.getAccountNumber())
                            .totalSold(bankAccountDto.getSold())
                            .build())
                .toList();
    }

    public Receivables getReceivables(Date fromDate, Date toDate) {

        Totals totals = calculateTotals(InvoiceDirection.IN, fromDate, toDate);
        return Receivables.builder()
                .totalToReceive(totals.totalOpen + totals.totalOverdue)
                .totalOpen(totals.totalOpen)
                .totalOverdue(totals.totalOverdue)
                .build();
    }

    public Payables getPayables(Date fromDate, Date toDate) {
        Totals totals = calculateTotals(InvoiceDirection.OUT, fromDate, toDate);
        return Payables.builder()
                .totalToPay(totals.totalOpen + totals.totalOverdue)
                .totalOpen(totals.totalOpen)
                .totalOverdue(totals.totalOverdue)
                .build();
    }


    private boolean isInvoiceOverdue(InvoiceDto invoice) {
        return invoice.getDueDate().after(new Date());
    }

    private Totals calculateTotals(InvoiceDirection direction, Date fromDate, Date toDate) {
        List<InvoiceDto> unpaidInvoices = invoiceService.findInvoices(
                QInvoiceEntity.invoiceEntity.direction.eq(direction)
                        .and(QInvoiceEntity.invoiceEntity.completed.eq(false))
                        .and(QInvoiceEntity.invoiceEntity.dueDate.between(fromDate, toDate))
        );

        float totalOpen = 0f;
        float totalOverdue = 0f;

        for (InvoiceDto invoice : unpaidInvoices) {
            float remainingAmount = invoice.getRemainingAmount();
            if (isInvoiceOverdue(invoice)) {
                totalOverdue += remainingAmount;
            } else {
                totalOpen += remainingAmount;
            }
        }

        return new Totals(totalOpen, totalOverdue);
    }

    private record Totals(float totalOpen, float totalOverdue) {
    }
}
