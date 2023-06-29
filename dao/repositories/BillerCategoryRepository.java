package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.insurance.BillerCategory;
import com.freecharge.financial.dto.enums.BillerCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillerCategoryRepository extends JpaRepository<BillerCategory, Long> {
    BillerCategory findByBillerCategoryType(BillerCategoryType billerCategoryType);
}
