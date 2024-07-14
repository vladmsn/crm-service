package com.mmdevelopement.crm.domain.financial.repository;

import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoicePaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoicePaymentRepository extends JpaRepository<InvoicePaymentEntity, Integer>, QuerydslPredicateExecutor<InvoicePaymentEntity> {
    List<InvoicePaymentEntity> findAllByInvoiceId(Integer invoiceId);
}
