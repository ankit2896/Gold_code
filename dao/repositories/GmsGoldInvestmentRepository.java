package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.GmsGoldInvestment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GmsGoldInvestmentRepository extends JpaRepository<GmsGoldInvestment, Long> {

    List<GmsGoldInvestment> findByGoalId(String goalId);

    GmsGoldInvestment findByTxnId(String txnId);


}
