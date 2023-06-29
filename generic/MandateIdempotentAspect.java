package com.freecharge.financial.generic;


import com.freecharge.financial.cache.RedisCacheManager;
import com.freecharge.financial.context.UserContextDirectory;
import com.freecharge.financial.dto.enums.ResponseCodes;
import com.freecharge.financial.dto.request.BillpaymentRequestSRO;
import com.freecharge.financial.dto.request.PaymentRequest;
import com.freecharge.financial.dto.request.RegisterUserRequest;
import com.freecharge.financial.exception.GoldServiceException;
import com.freecharge.financial.service.IUtilityService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.Valid;


@Aspect
@Component
@Order(1)
@Slf4j
public class MandateIdempotentAspect {


    @Autowired
    RedisCacheManager redisCacheManager;

    @Autowired
    private IUtilityService iUtilityService;


    @Around("@annotation(MandateIdempotent)")
    public Object idempotencyAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        log.info("Before invoking method : " + proceedingJoinPoint.getTarget());

        Object paramValues[] = proceedingJoinPoint.getArgs();

        Object requestObject = paramValues[0];

        if (requestObject instanceof RegisterUserRequest) {
            String imsPhone = UserContextDirectory.get().getImsMobile();
            return getObjectForUserRegisteration(proceedingJoinPoint, imsPhone);
        } else if (requestObject instanceof PaymentRequest) {
            PaymentRequest paymentRequest = (PaymentRequest) requestObject;
            String lookUpId = paymentRequest.getBlockId().toString();
            return getObject(proceedingJoinPoint, lookUpId);
        } else if (requestObject instanceof @Valid BillpaymentRequestSRO) {
            BillpaymentRequestSRO billpaymentRequestSRO = (BillpaymentRequestSRO) requestObject;
            String lookUpId = billpaymentRequestSRO.getBlockId().toString();
            return getObjectForInsurance(proceedingJoinPoint, lookUpId);

        }

        return null;
    }


    public Object getObjectForUserRegisteration(ProceedingJoinPoint proceedingJoinPoint, String phoneNo) throws Throwable {
        if (redisCacheManager.get(phoneNo) == null) {
            try {
                log.info("setting lock for phoneNo : " + phoneNo);
                Boolean isSet = redisCacheManager.setIfAbsent(phoneNo, true);
                log.info("lock set for phoneNo : " + phoneNo + ", isSet : " + isSet);
                if (isSet) {
                    return proceedingJoinPoint.proceed();
                } else {
                    throw new GoldServiceException(ResponseCodes.FAILED.getCode(), ResponseCodes.FAILED.getMessage());
                }
            } catch (GoldServiceException e) {
                log.error("Exception occurred in MandateIdempotentAspect : " + e.toString());
                //iUtilityService.sendEmailAlerts("Exception occurred in MandateIdempotentAspect : " + e.toString(),e);
                throw e;
            } finally {
                redisCacheManager.delete(phoneNo);
                log.info("removing lock for user  " + phoneNo);

            }
        } else {
            throw new GoldServiceException(ResponseCodes.FAILED.getCode(), ResponseCodes.FAILED.getMessage());
        }
    }


    public Object getObject(ProceedingJoinPoint proceedingJoinPoint, String lookupId) throws Throwable {
        if (redisCacheManager.get(lookupId) == null) {
            try {
                log.info("setting lock for blockId : " + lookupId);
                Boolean isSet = redisCacheManager.setIfAbsent(lookupId, true);
                log.info("lock set for blockId : " + lookupId + ", isSet :" + isSet);
                if (isSet) {
                    return proceedingJoinPoint.proceed();
                } else {
                    throw new GoldServiceException(ResponseCodes.RETRY.getCode(), ResponseCodes.RETRY.getMessage());
                }
            } catch (GoldServiceException e) {
                log.error("Exception occurred in MandateIdempotentAspect : " + e.toString());
                throw e;
            } finally {
                redisCacheManager.delete(lookupId);
                log.info("removing lock for transaction : " + lookupId);

            }
        } else {
            throw new GoldServiceException(ResponseCodes.RETRY.getCode(), ResponseCodes.RETRY.getMessage());
        }
    }


    public Object getObjectForInsurance(ProceedingJoinPoint proceedingJoinPoint, String lookupId) throws Throwable {
        if (redisCacheManager.get(lookupId) == null) {
            try {
                log.info("setting lock for blockId : " + lookupId);
                Boolean isSet = redisCacheManager.setIfAbsent(lookupId, true);
                log.info("lock set for blockId : " + lookupId + ", isSet :" + isSet);
                if (isSet) {
                    return proceedingJoinPoint.proceed();
                } else {
                    throw new Exception("unable to set lock : ");
                }
            } catch (Exception e) {
                log.error("Exception occurred in MandateIdempotentAspect for insurance : " + e.toString());
                throw e;
            } finally {
                redisCacheManager.delete(lookupId);
                log.info("removing lock for transaction : " + lookupId);

            }
        } else {
            throw new Exception("duplicate txn found");
        }
    }

    @Pointcut("within(com.freecharge.financial.service..*)")
    public void idempotencyPointcut() {
    }
}

