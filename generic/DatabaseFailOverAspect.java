package com.freecharge.financial.generic;


import com.freecharge.financial.cache.RedisCacheManager;
import com.freecharge.financial.constants.GoldConstant;
import com.freecharge.financial.dao.entities.ConfirmTransaction;
import com.freecharge.financial.dao.repositories.ConfirmTransactionRepository;
import com.freecharge.financial.dto.request.ConfirmOrderRequestSRO;
import com.freecharge.financial.dto.request.PaymentRequest;
import com.freecharge.financial.exception.FulfillmentServiceException;
import com.freecharge.financial.service.IUtilityService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@Order(0)
public class DatabaseFailOverAspect {

    @Autowired
    RedisCacheManager redisCacheManager;


    @Autowired
    private ConfirmTransactionRepository confirmTransactionRepository;

    @Autowired
    private IUtilityService iUtilityService;


    @Around("@annotation(com.freecharge.financial.generic.DatabaseFailOver)")
    public Object idempotencyAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        log.info("Before invoking method : " + proceedingJoinPoint.getTarget() + " Db Aspect called");


        Object paramValues[] = proceedingJoinPoint.getArgs();

        String lookupId = null;
        String orderId=null;

        Object requestObject = paramValues[0];

        if (requestObject instanceof PaymentRequest) {
            PaymentRequest paymentRequest = (PaymentRequest) requestObject;
            lookupId = paymentRequest.getBlockId().toString();
            orderId=paymentRequest.getOrderId();
        }

        if (requestObject instanceof @Valid ConfirmOrderRequestSRO) {
            ConfirmOrderRequestSRO confirmOrderRequestSRO = (ConfirmOrderRequestSRO) requestObject;
            lookupId = confirmOrderRequestSRO.getLookupId();
            orderId=confirmOrderRequestSRO.getOrderId();

        }

        String DB_EXC_KEY = GoldConstant.REDIS_DB_EXE_PRE_KEY + lookupId;

        try {

            log.info("looking for any db exception with key : " + DB_EXC_KEY);

            FulfillmentServiceException fulfillmentServiceException = redisCacheManager.get(DB_EXC_KEY);

            log.info("found db exception for : " + DB_EXC_KEY + " exception is : " + fulfillmentServiceException);

            if (fulfillmentServiceException != null) {


                switch (fulfillmentServiceException.getConfirmObjectType()) {

                    //handling for gold for the time being now..

                    case GOLD:
                        ConfirmTransaction confirmTransaction;

                        Long blockId = new Long(lookupId);
                        confirmTransaction = confirmTransactionRepository.findByBlockId(blockId);

                        log.info("confirmed  for details in db : " + confirmTransaction);

                        if (confirmTransaction == null) {
                            confirmTransaction = (ConfirmTransaction) fulfillmentServiceException.getConfirmObject();
                            log.info("Inserted in confirm transaction : {}",confirmTransaction);
                            confirmTransactionRepository.save(confirmTransaction);
                            log.info("inserted in db :" + confirmTransaction);
                        }
                        redisCacheManager.delete(DB_EXC_KEY);
                        log.info("key deleted from cache : " + DB_EXC_KEY);
                        break;
                    default:
                        break;
                }
            }

            return proceedingJoinPoint.proceed();

        } catch (Exception exception) {

            log.error("exception caught : " + exception);
            iUtilityService.sendEmailAlerts("Exception occur while DB Failover aspect",exception);

            if (exception instanceof FulfillmentServiceException) {

                FulfillmentServiceException fulfillmentServiceException = (FulfillmentServiceException) exception;
                log.error("fulfillment Service Exception : "+fulfillmentServiceException);
                if ( Objects.nonNull(fulfillmentServiceException) && !"null".equals(fulfillmentServiceException.getConfirmObjectType()) && fulfillmentServiceException.getConfirmObject() != null) {
                    log.error("setting execption in redis  : " + fulfillmentServiceException);
                    redisCacheManager.setIfAbsent(DB_EXC_KEY, fulfillmentServiceException);
                }
            }


            throw exception;
        }

    }


    @Pointcut("within(com.freecharge.financial.service..*)")
    public void idempotencyPointcut() {
    }

}
