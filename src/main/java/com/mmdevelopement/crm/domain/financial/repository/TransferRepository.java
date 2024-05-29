package com.mmdevelopement.crm.domain.financial.repository;

import com.mmdevelopement.crm.domain.financial.entity.accounts.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Integer> {
}
