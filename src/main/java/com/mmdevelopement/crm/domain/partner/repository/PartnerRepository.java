package com.mmdevelopement.crm.domain.partner.repository;

import com.mmdevelopement.crm.domain.partner.entity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity, Integer> {

    PartnerEntity findByName(String name);

    PartnerEntity findByCUI(String CUI);
}
