package com.freecharge.financial.exceptionhandler;

import com.freecharge.financial.exception.GoldServiceException;
import com.freecharge.financial.service.IUtilityService;
import com.freecharge.financial.utility.BaseError;
import com.freecharge.financial.utility.BaseResponseSRO;
import com.freecharge.financial.utility.GoldErrorCodes;
import com.freecharge.financial.utility.GoldUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GoldServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private IUtilityService iUtilityService;

    @ExceptionHandler({GoldServiceException.class, Exception.class})
    protected ResponseEntity<Object> handleServiceException(Exception ex, WebRequest webRequest) {

        BaseResponseSRO baseResponseSRO = new BaseResponseSRO();
        BaseError baseError = new BaseError();
        if (ex instanceof GoldServiceException) {
            GoldServiceException goldException = (GoldServiceException) ex;
            log.error("GoldException caught : " + goldException.getErrorCode() + " error Message : " + goldException.getErrorMsg(), ex);
            baseError.setErrorCode(goldException.getErrorCode());
            baseError.setErrorMessage(goldException.getErrorMsg());
            iUtilityService.sendEmailAlerts("GoldException caught : " + goldException.getErrorCode() + " error Message : " + goldException.getErrorMsg(), GoldUtils.getRequestMetdata(webRequest.toString(),null,baseError.toString()),ex);
        } else if(ex instanceof RedisConnectionFailureException){
            log.error("Can not connect with Jedis Sever ->{}",ex.getMessage());
            baseError.setErrorCode(GoldErrorCodes.UNABLE_TO_CONNECT_REDIS_SERVER.name());
             baseError.setErrorMessage(ex.getMessage());
            iUtilityService.sendEmailAlerts("Can not connect with Jedis Sever : "+ex.getMessage(), GoldUtils.getRequestMetdata(webRequest.toString(),null,baseError.toString()),ex);
        } else {
            log.error("General exception caught ", ex);
            baseError.setErrorCode(GoldErrorCodes.IN_ERR_500.name());
            baseError.setErrorMessage(Objects.nonNull(ex.getMessage())?ex.getMessage():GoldErrorCodes.IN_ERR_500.getErrorMessage());
            iUtilityService.sendEmailAlerts("General exception caught : "+(Objects.nonNull(ex.getMessage())?ex.getMessage():GoldErrorCodes.IN_ERR_500.getErrorMessage())
                    , GoldUtils.getRequestMetdata(webRequest.toString(),null,baseError.toString()),ex);
        }

        baseResponseSRO.setError(baseError);
        return handleExceptionInternal(ex, baseResponseSRO, new HttpHeaders(), HttpStatus.OK, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder errorBuilder = new StringBuilder();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            errorBuilder.append(error.getDefaultMessage());
            break;
        }
        BaseResponseSRO baseResponseSRO = new BaseResponseSRO();
        BaseError baseError = new BaseError();
        baseError.setErrorCode(GoldErrorCodes.IN_ERR_4001.name());
        baseError.setErrorMessage(errorBuilder.toString());

        baseResponseSRO.setError(baseError);
        log.info("argument map exception : " + baseResponseSRO);
        iUtilityService.sendEmailAlerts("Argument map exception : "+ex.getMessage(), GoldUtils.getRequestMetdata(request.toString(),null,baseResponseSRO.toString()),ex);
        return handleExceptionInternal(ex, baseResponseSRO, new HttpHeaders(), HttpStatus.OK, request);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        log.error("handleHttpMessageNotReadable:  ", ex);
        BaseResponseSRO baseResponseSRO = new BaseResponseSRO();
        BaseError baseError = new BaseError();
        baseError.setErrorCode(GoldErrorCodes.IN_ERR_500.name());
        baseError.setErrorMessage(GoldErrorCodes.IN_ERR_500.getErrorMessage());
        baseResponseSRO.setError(baseError);
        iUtilityService.sendEmailAlerts("handleHttpMessageNotReadable : "+ex.getMessage(), GoldUtils.getRequestMetdata(request.toString(),null,baseResponseSRO.toString()),ex);
        return handleExceptionInternal(ex, baseResponseSRO, new HttpHeaders(), HttpStatus.OK, request);
    }
}
