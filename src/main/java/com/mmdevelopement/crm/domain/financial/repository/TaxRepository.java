package com.mmdevelopement.crm.domain.financial.repository;

import com.mmdevelopement.crm.domain.financial.entity.TaxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepository extends JpaRepository<TaxEntity, Integer> {
}
