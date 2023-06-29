package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.DispatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DispatchStatusRepository extends JpaRepository<DispatchStatus, Long> {
    public Optional<DispatchStatus> findById(Long txnId);

    @Query(value = "select * from dispatch_status_details where created >= NOW() - INTERVAL 10 DAY", nativeQuery = true)
    public  List<DispatchStatus> findAllDispatchedTxnBeforeTenDays();

}
