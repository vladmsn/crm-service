package com.mmdevelopement.crm.domain.financial.repository;

import com.mmdevelopement.crm.domain.financial.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
}
