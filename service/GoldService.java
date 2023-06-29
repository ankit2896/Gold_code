package com.freecharge.financial.service;


import com.freecharge.financial.dto.request.*;
import com.freecharge.financial.dto.response.*;
import com.freecharge.financial.exception.GoldServiceException;
import com.freecharge.intg.response.UpdateMobileNumberResponse;
import com.snapdeal.payments.disbursement.model.PennyDropResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;

public interface GoldService {
    ReturningUserDelivery fetchBalanceForReturningUser(HttpServletRequest httpRequest) throws GoldServiceException;

    FetchBalanceResponse fetchBalance(boolean userMetaDataRequired,HttpServletRequest httpRequest) throws GoldServiceException;

    RegisterUserResponse registerUser(RegisterUserRequest registerUserRequest) throws GoldServiceException;

    BuyPrice getBuyPrice() throws GoldServiceException;
    SellPrice getSellPrice() throws GoldServiceException;
    BuyPrice getBuyPrice(String goldPurity) throws GoldServiceException;

    SellPrice getSellPrice(String goldPurity) throws GoldServiceException;

    VerifyPriceResponse verifyGoldPrice(VeriyPriceRequest priceRequest, HttpServletRequest httpServletRequest) throws GoldServiceException;

    PaymentResponse checkPaymentStatus(PaymentRequest paymentRequest) throws GoldServiceException;

    BlockTransactionResponse getTransactionResponse(BlockTransactionRequest transactionRequest) throws GoldServiceException;

    PublishBankDetailsResponse publishBankDetails(PublishBankDetailsRequest publishBankDetailsRequest) throws GoldServiceException;

    GetInvoiceResponse fetchInvoice(@Valid GetInvoiceRequest getInvoiceRequest) throws GoldServiceException;

    CheckBankDetailsResponse checkBankDetails() throws GoldServiceException;

    FulfillmentDetailsResponse findDetails(FulfillmentDetailsRequest orderId,HttpServletRequest httpServletRequest) throws GoldServiceException;

	GoldSellCallbackResponse handelSellCallback(GoldSellCallbackRequest sellCallbackRequest);

	FulfillmentDetailsResponse findOrderDetails(FulfillmentDetailsRequest request) throws GoldServiceException;

    UpdateMobileNumberResponse updateMobileNumber(String newMobile, String oldMobile) throws GoldServiceException;

    FetchBalanceResponse fetchBalanceByMobileNumber(String mobileNumber,HttpServletRequest httpRequest) throws GoldServiceException;

    VerifyPhoneUpdateResponse verifyPhoneUpdate(String mobileNumber) throws GoldServiceException;

    public ValidatePincodeResponse validatePinCode(String pincode) throws GoldServiceException;

    public ProductDetailsResponse getRedeemProduct(RedeemRequest redeemRequest) throws GoldServiceException;

    public AddressDetailsResponse fetchAllUserAddress() throws GoldServiceException;

    public RedeemGoldDispatchStatusResponse redeemGoldDispatchStatus(String orderId) throws GoldServiceException;

    public void flushRedeemProductDetails();

    public void recordPolicy(String imsId);
    
    GoldTransactionResponse getGoldTransactionHistory(TransactionRequest request,HttpServletRequest httpRequest) throws GoldServiceException;

    GraphDataResponse getGraphData(String type,String period) throws GoldServiceException, IOException;

    BankDetailResponse getBankDetails(String imsUserId) throws GoldServiceException;

    FaqResponse getFaq() throws GoldServiceException;

    PennyDropResponse pennydrop(PennyDropRequestGC request) throws GoldServiceException;

    PublishBankDetailsResponse publishBankDetailsV2(PublishBankDetailsRequestV2 request) throws GoldServiceException;

    GoldBannerResponse getGoldBanner() throws GoldServiceException;

    AddressDetailsResponse addOrUpdateAddress(RequestAddress requestAddress) throws GoldServiceException;

    AddressDetailsResponse deleteAddress(String addressId) throws GoldServiceException;

    GoldTransactionResponse getGoldTransactionHistoryByMobileNumber(GoldBalanceRequest request,HttpServletRequest httpRequest) throws GoldServiceException;

    Set<UpdateMobileNumberDto> getErrorInUpdateMobileNumbers();


    void updateAllDispatchStatuses();
    InvestmentDetailsResponse fetchGMSInvestmentDetails(@Valid GMSInvestmentDetailsRequest request) throws GoldServiceException,IOException;

    InvestmentReturnsResponse getCurrentInvestment(@Valid InvestmentReturnsRequest request) throws GoldServiceException, IOException;

    UserKYCResponse completeUserKYC(UserKYCRequest request) throws GoldServiceException;

    UserPanDetailsResponse getUserPanDetails() throws GoldServiceException ;

    DeliveryInformationResponse fetchDeliveryInformation(@Valid DeliveryInformationRequest request) throws GoldServiceException;

    GoldDeliveryInformationResponse fetchGoldDeliveryInformation(String orderId) throws GoldServiceException;


    void updateSellTransactionStatus();

    SellTransactionStatusResponse settleSellTransactionBetween(SellTransactionsRequest request) throws GoldServiceException;
    GoldUserStateResponse checkUserState(GoldUserStateRequest goldUserStateRequest) throws GoldServiceException;


}
