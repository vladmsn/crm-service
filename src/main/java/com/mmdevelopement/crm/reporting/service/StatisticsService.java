package com.mmdevelopement.crm.reporting.service;


import com.mmdevelopement.crm.config.security.context.RequestContextHolder;
import com.mmdevelopement.crm.domain.financial.entity.dto.CategoryDto;
import com.mmdevelopement.crm.domain.financial.entity.enums.InvoiceDirection;
import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoiceEntity;
import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoicePaymentEntity;
import com.mmdevelopement.crm.domain.financial.service.BankAccountService;
import com.mmdevelopement.crm.domain.financial.service.CategoryService;
import com.mmdevelopement.crm.domain.financial.service.InvoiceService;
import com.mmdevelopement.crm.domain.partner.entity.dto.PartnerDto;
import com.mmdevelopement.crm.domain.partner.service.PartnerService;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import com.mmdevelopement.crm.reporting.entity.AggregatedStatistics;
import com.mmdevelopement.crm.reporting.entity.PartnerStatistics;
import com.mmdevelopement.crm.reporting.entity.PartnerStatisticsRequest;
import com.mmdevelopement.crm.reporting.entity.items.AccountSoldItem;
import com.mmdevelopement.crm.reporting.entity.items.CashFlowItem;
import com.mmdevelopement.crm.reporting.entity.items.ExpensesByCategoryItem;
import com.mmdevelopement.crm.reporting.entity.items.IncomeByCategoryItem;
import com.mmdevelopement.crm.reporting.entity.items.Payables;
import com.mmdevelopement.crm.reporting.entity.items.Receivables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    public static final String VENIT = "Venit";
    public static final String CHELTUIALA = "Cheltuiala";
    private final CategoryService categoryService;
    private final BankAccountService bankAccountService;
    private final InvoiceService invoiceService;
    private final PartnerService partnerService;

    public AggregatedStatistics getDashboardData(Date fromDate, Date toDate) {
        log.info("Getting dashboard data for tenant {} from {} to {}", RequestContextHolder.getCurrentTenantId(), fromDate, toDate);

        List<InvoiceEntity> invoices = invoiceService.findInvoicesBetweenDates(fromDate, toDate);
        List<InvoicePaymentEntity> payments = invoiceService.findPaymentsByPaymentDateBetween(fromDate, toDate);
        List<CategoryDto> categories = categoryService.getAllCategories(true);

        return AggregatedStatistics.builder()
                .accountsSold(getAccountsSold())
                .receivables(getReceivables(fromDate, toDate, invoices))
                .payables(getPayables(fromDate, toDate, invoices))
                .cashFlow(getCashFlow(payments))
                .incomeByCategory(getIncomeByCategory(payments, categories))
                .expensesByCategory(getExpensesByCategory(payments, categories))
                .build();
    }

    public PartnerStatistics getPartnerStatistics(PartnerStatisticsRequest request) {

        log.info("Getting partner statistics for tenant {} from {} to {} for partner {}",
                RequestContextHolder.getCurrentTenantId(), request.getFromDate(), request.getToDate(), request.getPartnerId());

        List<PartnerDto> partners = partnerService.findPartners();
        List<InvoiceEntity> invoices = invoiceService.findInvoicesBetweenDates(request.getFromDate(), request.getToDate());
        List<InvoicePaymentEntity> payments = invoiceService.findPaymentsByPaymentDateBetween(request.getFromDate(), request.getToDate());

        final CashFlowWrapper cashFlowWrapper = new CashFlowWrapper();

        payments.stream()
                .filter(payment -> payment.invoiceId() != null)
                .filter(payment -> {
                    for (InvoiceEntity invoice : invoices) {
                        if (invoice.id().equals(payment.invoiceId())) {
                            if (invoice.partnerId().equals(request.getPartnerId())) {
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .forEach(payment -> {
                    if (Objects.equals(payment.paymentDirection(), InvoiceDirection.IN.name())) {
                        cashFlowWrapper.totalReceived += payment.amount();
                    } else {
                        cashFlowWrapper.totalPayed += payment.amount();
                    }
                });

        String partnerName = partners.stream().filter(partner -> partner.getId().equals(request.getPartnerId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Partner not found")).getName();

        return PartnerStatistics.builder()
                .partnerName(partnerName)
                .totalReceived(cashFlowWrapper.totalReceived)
                .totalPaid(cashFlowWrapper.totalPayed)
                .build();
    }

    public List<AccountSoldItem> getAccountsSold() {
        return bankAccountService.getAllAccounts()
                .stream()
                .map(bankAccountDto ->  AccountSoldItem.builder()
                            .accountName(bankAccountDto.getName())
                            .totalSold(bankAccountDto.getSold())
                            .build())
                .toList();
    }

    public Receivables getReceivables(Date fromDate, Date toDate, List<InvoiceEntity> invoices) {
        Totals totals = calculateTotals(InvoiceDirection.OUT, fromDate, toDate, invoices);
        return Receivables.builder()
                .totalToReceive(totals.totalOpen + totals.totalOverdue)
                .totalOpen(totals.totalOpen)
                .totalOverdue(totals.totalOverdue)
                .build();
    }

    public Payables getPayables(Date fromDate, Date toDate, List<InvoiceEntity> invoices) {
        Totals totals = calculateTotals(InvoiceDirection.IN, fromDate, toDate, invoices);
        return Payables.builder()
                .totalToPay(totals.totalOpen + totals.totalOverdue)
                .totalOpen(totals.totalOpen)
                .totalOverdue(totals.totalOverdue)
                .build();
    }

    private Map<Integer, Float> getAmountByCategory(String categoryType, List<InvoicePaymentEntity> payments, List<CategoryDto> categories) {
        Map<Integer, Float> amountByCategory = new HashMap<>();

        for (InvoicePaymentEntity payment : payments) {
            Integer categoryId = payment.categoryId();

            if (categoryId == null) {
                continue;
            }

            var category = categories.stream().filter(cat -> cat.getId().equals(categoryId)).findFirst().orElse(null);

            if (category != null && category.getType().equals(categoryType)) {
                float amount = payment.amount();
                float existingAmount = amountByCategory.getOrDefault(categoryId, 0f);
                amountByCategory.put(categoryId, existingAmount + amount);
            }
        }

        return amountByCategory;
    }

    public List<IncomeByCategoryItem> getIncomeByCategory(List<InvoicePaymentEntity> payments, List<CategoryDto> categories) {
        Map<Integer, Float> incomeByCategory = getAmountByCategory(VENIT, payments, categories);

        return incomeByCategory.entrySet().stream()
                .map(entry -> {
                    var category = categories.stream().filter(cat -> cat.getId().equals(entry.getKey())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Category not found"));
                    return IncomeByCategoryItem.builder()
                            .categoryName(category.getName())
                            .colorCode(category.getColorCode())
                            .income(entry.getValue())
                            .build();
                })
                .toList();
    }

    public List<ExpensesByCategoryItem> getExpensesByCategory(List<InvoicePaymentEntity> payments, List<CategoryDto> categories) {
        Map<Integer, Float> expenseByCategory = getAmountByCategory(CHELTUIALA, payments, categories);

        return expenseByCategory.entrySet().stream()
                .map(entry -> {
                    var category = categories.stream().filter(cat -> cat.getId().equals(entry.getKey())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Category not found"));
                    return ExpensesByCategoryItem.builder()
                            .categoryName(category.getName())
                            .colorCode(category.getColorCode())
                            .expenses(entry.getValue())
                            .build();
                })
                .toList();
    }

    private boolean isInvoiceOverdue(InvoiceEntity invoice) {
        var today = new Date();
        return invoice.dueDate().before(today);
    }

    private Totals calculateTotals(InvoiceDirection direction, Date fromDate, Date toDate, List<InvoiceEntity> invoices) {
        List<InvoiceEntity> unpaidInvoices = invoices.stream()
                .filter(invoice -> invoice.direction().equals(direction.toString())
                        && !invoice.completed()
                        && invoice.invoiceDate().after(fromDate)
                        && invoice.invoiceDate().before(toDate))
                .toList();

        float totalOpen = 0f;
        float totalOverdue = 0f;

        for (InvoiceEntity invoice : unpaidInvoices) {
            float remainingAmount = invoice.remainingAmount();
            if (isInvoiceOverdue(invoice)) {
                totalOverdue += remainingAmount;
            } else {
                totalOpen += remainingAmount;
            }
        }

        return new Totals(totalOpen, totalOverdue);
    }

    public List<CashFlowItem> getCashFlow(List<InvoicePaymentEntity> payments) {
        Map<String, CashFlowItem> cashFlowMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (InvoicePaymentEntity payment : payments) {
            LocalDate paymentDate = payment.paymentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String monthKey = paymentDate.format(formatter);
            int year = paymentDate.getYear();
            int month = paymentDate.getMonthValue();

            CashFlowItem existingItem = cashFlowMap.get(monthKey);
            float income = (Objects.equals(payment.paymentDirection(), InvoiceDirection.IN.name())) ? payment.amount() : 0;
            float expense = (Objects.equals(payment.paymentDirection(), InvoiceDirection.OUT.name())) ? payment.amount() : 0;

            if (existingItem != null) {
                income += existingItem.income();
                expense += existingItem.expenses();
            }

            CashFlowItem cashFlowItem = new CashFlowItem(String.valueOf(month), year, income, expense);
            cashFlowMap.put(monthKey, cashFlowItem);
        }

        return cashFlowMap.values().stream()
                .sorted(Comparator.comparing(CashFlowItem::year).thenComparing(CashFlowItem::month))
                .toList();
    }

    private record Totals(float totalOpen, float totalOverdue) {
    }

    private static class CashFlowWrapper {
        float totalReceived = 0;
        float totalPayed = 0;
    }
}
