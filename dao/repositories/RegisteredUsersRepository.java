package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.UserRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredUsersRepository extends PagingAndSortingRepository<UserRegistration, Long> {

    UserRegistration findByImsPhoneNo(String imsPhone);

    Optional<UserRegistration> findById(Long aggregateUserId);

    UserRegistration findByAccountNo(String accountNo);

    UserRegistration findByImsEmailId(String imsEmailId);

    UserRegistration findByImsUserId(Integer imsUserId);

    UserRegistration findByAggregatorUserId(String aggregateUserId);

    Page<UserRegistration> findAll(Pageable pageable);


}
