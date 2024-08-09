package com.mmdevelopement.crm.domain.financial.repository;

import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer>, QuerydslPredicateExecutor<InvoiceEntity> {
    Optional<InvoiceEntity> findFirstByDirectionOrderByIdDesc(String name);

    List<InvoiceEntity> findAllByInvoiceDateBetween(Date startDate, Date endDate);
}
