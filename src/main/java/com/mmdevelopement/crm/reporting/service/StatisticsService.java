package com.mmdevelopement.crm.reporting.service;


import com.mmdevelopement.crm.config.security.context.RequestContextHolder;
import com.mmdevelopement.crm.domain.financial.entity.dto.CategoryDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.InvoiceDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.InvoicePaymentDto;
import com.mmdevelopement.crm.domain.financial.entity.enums.CategoryType;
import com.mmdevelopement.crm.domain.financial.entity.enums.InvoiceDirection;
import com.mmdevelopement.crm.domain.financial.entity.invoices.QInvoiceEntity;
import com.mmdevelopement.crm.domain.financial.entity.invoices.QInvoicePaymentEntity;
import com.mmdevelopement.crm.domain.financial.service.BankAccountService;
import com.mmdevelopement.crm.domain.financial.service.CategoryService;
import com.mmdevelopement.crm.domain.financial.service.InvoiceService;
import com.mmdevelopement.crm.domain.partner.service.PartnerService;
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
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    public static final String VENIT = "venit";
    public static final String CHELTUIALA = "cheltuiala";
    private final CategoryService categoryService;
    private final BankAccountService bankAccountService;
    private final InvoiceService invoiceService;
    private final PartnerService partnerService;

    public AggregatedStatistics getDashboardData(Date fromDate, Date toDate) {
        log.info("Getting dashboard data for tenant {} from {} to {}", RequestContextHolder.getCurrentTenantId(), fromDate, toDate);

        return AggregatedStatistics.builder()
                .accountsSold(getAccountsSold())
                .receivables(getReceivables(fromDate, toDate))
                .payables(getPayables(fromDate, toDate))
                .cashFlow(getCashFlow(fromDate, toDate))
                .incomeByCategory(getIncomeByCategory(fromDate, toDate))
                .expensesByCategory(getExpensesByCategory(fromDate, toDate))
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

    private Map<Integer, Float> getAmountByCategory(String categoryType, Date fromDate, Date toDate) {
        List<InvoicePaymentDto> payments = invoiceService.findPayments(QInvoicePaymentEntity.invoicePaymentEntity.paymentDate.between(fromDate, toDate));

        Map<Integer, Float> amountByCategory = new HashMap<>();

        Map<Integer, CategoryDto> categories = categoryService.getAllCategories()
                .stream()
                .collect(Collectors.toMap(CategoryDto::getId, Function.identity()));

        for (InvoicePaymentDto payment : payments) {
            Integer categoryId = payment.getCategoryId();

            if (categoryId == null) {
                continue;
            }

            var category = categories.get(categoryId);

            if (category.getType().equals(categoryType)) {
                float amount = payment.getAmount();
                float existingAmount = amountByCategory.getOrDefault(categoryId, 0f);
                amountByCategory.put(categoryId, existingAmount + amount);
            }
        }

        return amountByCategory;
    }

    public List<IncomeByCategoryItem> getIncomeByCategory(Date fromDate, Date toDate) {
        Map<Integer, Float> incomeByCategory = getAmountByCategory(VENIT, fromDate, toDate);

        return incomeByCategory.entrySet().stream()
                .map(entry -> {
                    var category = categoryService.getCategoryById(entry.getKey());

                    return IncomeByCategoryItem.builder()
                            .categoryName(category.getName())
                            .colorCode(category.getColorCode())
                            .income(entry.getValue())
                            .build();
                })
                .toList();
    }

    public List<ExpensesByCategoryItem> getExpensesByCategory(Date fromDate, Date toDate) {
        Map<Integer, Float> expenseByCategory = getAmountByCategory(CHELTUIALA, fromDate, toDate);

        return expenseByCategory.entrySet().stream()
                .map(entry -> {
                    var category = categoryService.getCategoryById(entry.getKey());

                    return ExpensesByCategoryItem.builder()
                            .categoryName(category.getName())
                            .colorCode(category.getColorCode())
                            .expenses(entry.getValue())
                            .build();
                })
                .toList();
    }


    public PartnerStatistics getPartnerStatistics(PartnerStatisticsRequest request) {

        log.info("Getting partner statistics for tenant {} from {} to {} for partner {}",
                RequestContextHolder.getCurrentTenantId(), request.getFromDate(), request.getToDate(), request.getPartnerId());

        final CashFlowWrapper cashFlowWrapper = new CashFlowWrapper();

        invoiceService.findPayments(QInvoicePaymentEntity.invoicePaymentEntity
                .paymentDate.between(request.getFromDate(), request.getToDate())
                .and(QInvoicePaymentEntity.invoicePaymentEntity.partnerId.eq(request.getPartnerId())))
                .forEach(payment -> {
                    if (payment.getDirection() == InvoiceDirection.IN) {
                        cashFlowWrapper.totalReceived += payment.getAmount();
                    } else {
                        cashFlowWrapper.totalPayed += payment.getAmount();
                    }
                });

        String partnerName = partnerService.getPartnerById(request.getPartnerId()).getName();

        return PartnerStatistics.builder()
                .partnerName(partnerName)
                .totalReceived(cashFlowWrapper.totalReceived)
                .totalPayed(cashFlowWrapper.totalPayed)
                .build();
    }


    private boolean isInvoiceOverdue(InvoiceDto invoice) {
        return invoice.getDueDate().after(new Date());
    }

    private Totals calculateTotals(InvoiceDirection direction, Date fromDate, Date toDate) {
        List<InvoiceDto> unpaidInvoices = invoiceService.findInvoices(
                QInvoiceEntity.invoiceEntity.direction.eq(direction.toString())
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

    public List<CashFlowItem> getCashFlow(Date fromDate, Date toDate) {
        List<InvoicePaymentDto> payments = invoiceService.findPayments(QInvoicePaymentEntity.invoicePaymentEntity.paymentDate.between(fromDate, toDate));

        Map<String, CashFlowItem> cashFlowMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (InvoicePaymentDto payment : payments) {
            LocalDate paymentDate = payment.getPaymentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String monthKey = paymentDate.format(formatter);
            int year = paymentDate.getYear();
            int month = paymentDate.getMonthValue();

            CashFlowItem existingItem = cashFlowMap.get(monthKey);
            float income = (payment.getDirection() == InvoiceDirection.IN) ? payment.getAmount() : 0;
            float expense = (payment.getDirection() == InvoiceDirection.OUT) ? payment.getAmount() : 0;

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
