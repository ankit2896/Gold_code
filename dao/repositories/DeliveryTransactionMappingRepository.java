package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.DeliveryTransactionMapping;
import com.freecharge.financial.dao.entities.SellTransactionLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryTransactionMappingRepository extends JpaRepository<DeliveryTransactionMapping, Long> {
    DeliveryTransactionMapping findByTxnId(String txnId);

    DeliveryTransactionMapping findByTxnIdAndAddressId(String txnId,String addressId);

}
