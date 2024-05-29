package com.mmdevelopement.crm.domain.financial.repository;

import com.mmdevelopement.crm.domain.financial.entity.ElementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElementRepository extends JpaRepository<ElementEntity, Integer> {
}
