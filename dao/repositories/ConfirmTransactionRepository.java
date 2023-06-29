package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.ConfirmTransaction;
import com.freecharge.financial.dto.enums.OrderStatus;
import com.freecharge.financial.dto.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfirmTransactionRepository extends JpaRepository<ConfirmTransaction, Long> {

    ConfirmTransaction findByOrderId(String orderId);

    ConfirmTransaction findByBlockId(Long blockId);

    ConfirmTransaction findByBlockIdAndTransactionStatus(Long blockId, OrderStatus status);

    List<ConfirmTransaction> findByUserId(String userId);

    List<ConfirmTransaction> findByBlockIdIn(List<Long> ids);

    List<ConfirmTransaction> findByBlockIdInAndTransactionStatusIn(List<Long> ids,List<OrderStatus> status);

    @Query(value = "select * from confirm_transaction where user_id= :agg_user_id and transaction_type='SELL' and " +
            "created >= NOW() - INTERVAL 1 DAY and transaction_status in ('SUCCESS','DEEMED_SUCCESS')",nativeQuery = true)
    List<ConfirmTransaction> findUserPerDayTxns(@Param(value = "agg_user_id") String aggUserId);

    @Query(value = "SELECT  DISTINCT user_id from  confirm_transaction where transaction_type='BUY' and transaction_status ='SUCCESS'", nativeQuery = true)
    List<String> findAllInvestedUsers();

    List<ConfirmTransaction> findByUserIdAndTransactionTypeIn(String userId, List<TransactionType> type);


    List<ConfirmTransaction> findByTransactionTypeInAndTransactionStatusNotIn(List<TransactionType> types,List<OrderStatus> statuses);

    @Query(value = "select * from confirm_transaction where transaction_type in ('REDEEM_GOLD','BUY_REDEEM_GOLD') and  created >= NOW() - INTERVAL 10 DAY and transaction_status in ('SUCCESS')", nativeQuery = true)
    List<ConfirmTransaction> findAllDeliveryTxnBeforeTenDays();


    ConfirmTransaction findByBlockIdAndUserId(Long blockId, String aggregatorUserId);
}
