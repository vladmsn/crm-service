package com.mmdevelopement.crm.domain.financial.repository;

import com.mmdevelopement.crm.domain.financial.entity.invoices.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {
}
