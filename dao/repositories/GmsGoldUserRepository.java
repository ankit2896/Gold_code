package com.freecharge.financial.dao.repositories;

import com.freecharge.financial.dao.entities.GmsGoldUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GmsGoldUserRepository extends JpaRepository<GmsGoldUser, Long> {

    GmsGoldUser findByGoalId(String goalId);

    boolean existsByGoalId(String goalId);

    boolean existsByGoalIdAndUserId(String goalId, String userId);

    List<GmsGoldUser> findByAggregatorUserId(String aggregatorUserId);

}
