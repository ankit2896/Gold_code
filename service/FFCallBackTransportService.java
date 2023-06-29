package com.freecharge.financial.service;

import com.freecharge.financial.dto.request.FFCallBackRequest;

public interface FFCallBackTransportService {
    
    public String executeCallback(FFCallBackRequest callBackReq) throws Exception;
}
