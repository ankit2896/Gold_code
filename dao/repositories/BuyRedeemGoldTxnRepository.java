package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.BuyRedeemGoldTransactionMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyRedeemGoldTxnRepository extends JpaRepository<BuyRedeemGoldTransactionMapping,Long> {
    BuyRedeemGoldTransactionMapping findByBlockId(String blockId);
}
