package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.BlockTransaction;
import com.freecharge.financial.dto.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockTransactionRepository extends JpaRepository<BlockTransaction, Long> {

    Optional<BlockTransaction> findById(Long uuid);
    BlockTransaction findByTxnId(String  txnId);
    List<BlockTransaction> findByTxnIdIn(List<String> ids);
    List<BlockTransaction> findByIdIn(List<Long> ids);
    List<BlockTransaction> findByIdInAndTransactionTypeIn(List<Long> ids , List<TransactionType> transactionTypes);
    BlockTransaction findByIdAndTxnId(Long id , String  txnId);

}
