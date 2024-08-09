package com.mmdevelopement.crm.reporting.entity;

import com.mmdevelopement.crm.reporting.entity.items.AccountSoldItem;
import com.mmdevelopement.crm.reporting.entity.items.CashFlowItem;
import com.mmdevelopement.crm.reporting.entity.items.ExpensesByCategoryItem;
import com.mmdevelopement.crm.reporting.entity.items.IncomeByCategoryItem;
import com.mmdevelopement.crm.reporting.entity.items.Payables;
import com.mmdevelopement.crm.reporting.entity.items.Receivables;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AggregatedStatistics {
    private List<AccountSoldItem> accountsSold;
    private Receivables receivables;
    private Payables payables;
    private List<CashFlowItem> cashFlow;
    private List<IncomeByCategoryItem> incomeByCategory;
    private List<ExpensesByCategoryItem> expensesByCategory;
}
