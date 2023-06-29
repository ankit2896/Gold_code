package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.common.FCAuthentication;
import com.freecharge.financial.dto.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<FCAuthentication, Long> {

    @Query("from FCAuthentication tk where tk.provider= :provider and enabled=true")
    FCAuthentication findByProvider(@Param(value = "provider") Provider provider);
    
    @Modifying
    @Query("update FCAuthentication tk set tk.enabled=false where tk.provider= :provider")
    void disable(@Param(value = "provider") Provider provider);
}
