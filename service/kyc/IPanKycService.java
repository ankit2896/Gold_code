package com.freecharge.financial.service.kyc;

import com.freecharge.financial.dto.response.PanDetailsResponse;
import com.freecharge.financial.dto.response.UserPanDetailsResponse;
import com.freecharge.financial.exception.GoldServiceException;
import com.freecharge.kyc.response.VerifyAndUpdatePoiResponse;

public interface IPanKycService {
    UserPanDetailsResponse getUserPanDetailsById(String imsId) throws GoldServiceException;

    PanDetailsResponse getDetailsByPanNo(String panNumber) throws GoldServiceException;

    VerifyAndUpdatePoiResponse verifyAndUpdatePanDetails(String imsId, String panNumber, String panName) throws GoldServiceException;
}
