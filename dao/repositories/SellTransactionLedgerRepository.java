package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.SellTransactionLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SellTransactionLedgerRepository extends JpaRepository<SellTransactionLedger, Long> {

	SellTransactionLedger findByTxId(Long txId);
	SellTransactionLedger findByBlockId(Integer blockId);
	SellTransactionLedger findByOrderId(String orderId);
	//List<SellTransactionLedger> getSellTxnBetweenDuration();

	@Query( value = "select * from sell_transaction_ledger  where created >= NOW() - Interval  3 DAY and created <= NOW() - Interval 1 DAY and transaction_status='PENDING' ",nativeQuery = true)
	List<SellTransactionLedger> getLatestThreeDayPendingTransaction();

	@Query(" select a from sell_transaction_ledger a where a.created >= :createdFrom  and a.created <= :createdTo and a.transactionStatus='PENDING' ")
	List<SellTransactionLedger> getSellTxnBetweenCreatedDuration(@Param("createdFrom") Date createdFrom , @Param("createdTo") Date createdTo);

	@Query("select a from sell_transaction_ledger a where a.transactionStatus='SETTLED' and a.updated >= :updatedFrom and a.updated <= :updatedTo")
    List<SellTransactionLedger> getSellTxnBetweenDuration(@Param("updatedFrom") Date updatedFrom, @Param("updatedTo") Date updatedTo);

}
