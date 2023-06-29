package com.freecharge.financial.service.gms;

import com.freecharge.financial.dao.entities.UserRegistration;
import com.freecharge.financial.dto.enums.TransactionType;
import com.freecharge.financial.dto.request.GMSRequest;
import com.freecharge.financial.exception.GoldServiceException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

public interface GMSService {
    void registerGMSUser(String goalId, UserRegistration user) throws GoldServiceException;

    boolean isRegisterGMSUserInGold(String goalId);

    void addInvestmentForGMS(@Valid GMSRequest gmsRequest) throws GoldServiceException;

    void saveGMSInvestmentInGold(@Valid GMSRequest gmsRequest) throws GoldServiceException;

    void addOrDeductGoldBalanceFromGoal(BigDecimal goldBalance, String goalId, TransactionType type) throws GoldServiceException;

    void addOrDeductGoldBalanceFromGoal(String txnId) throws GoldServiceException;

    GMSRequest getGMSRequest(String txnId , String imsPhoneNo ) throws GoldServiceException;

    BigDecimal getTotalInvestmentByGMS(String aggregatorUserId) throws  GoldServiceException;

    BigDecimal getInvestmentByGoalId(String goalId) throws GoldServiceException;

    List<String> getAllInvestmentOrderIdByGoalId(String goalId) throws GoldServiceException;

    List<String> getAllInvestmentOrderIdByGMS(String aggregatorUserId) throws  GoldServiceException;
}
