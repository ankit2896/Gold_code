package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.UserBankAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBankAccountDetailsRepository extends JpaRepository<UserBankAccountDetails, Long> {
    UserBankAccountDetails findByAccountToken(String accountToken);
    UserBankAccountDetails  findByAccountNo(String accountNo);
}
