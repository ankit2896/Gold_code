package com.freecharge.financial.controller;

import com.freecharge.financial.constants.GoldConstant;
import com.freecharge.financial.dto.enums.ResponseCodes;
import com.freecharge.financial.dto.request.*;
import com.freecharge.financial.dto.response.*;
import com.freecharge.financial.exception.GoldServiceException;
import com.freecharge.financial.service.GoldService;
import com.freecharge.financial.serviceimpl.GoldCallbackService;
import com.freecharge.financial.serviceimpl.gms.InvestmentReturnUtils;
import com.freecharge.financial.utility.GoldUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/gold/api/admin/")
@Tag(name = "Gold callback Controller", description = "Gold Admin Controller")
public class GoldAdminController {

    private HashMap<String, Object> response = null;

    @Autowired
    private GoldService goldService;

    @Autowired
    private GoldCallbackService goldCallbackService;

    @Autowired
    private InvestmentReturnUtils investmentReturnUtils;

    @Operation(summary = "sell callback", description = "status:1 success else failed")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = GoldSellCallbackResponse.class))})
    @RequestMapping(value = "callback/sell", method = RequestMethod.POST)
    public GoldSellCallbackResponse checkTransactionStatus(@RequestBody GoldSellCallbackRequest sellCallbackRequest) {
        GoldSellCallbackResponse response = goldService.handelSellCallback(sellCallbackRequest);
        log.info("GoldSellCallbackResponse :" + response);

        if (GoldConstant.CALLBACK_SUCCESS.equalsIgnoreCase(response.getStatus().toString()))
            goldCallbackService.executeCallback(sellCallbackRequest.getTxId());

        return response;
    }

    @Operation(summary = "Return fulfillment details")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = FulfillmentDetailsResponse.class))})
    @RequestMapping(value = "fulfillment/details", method = RequestMethod.POST)
    public FulfillmentDetailsResponse findDetails(@RequestBody FulfillmentDetailsRequest request) {
        log.info("Calling findDetails");
        FulfillmentDetailsResponse response = new FulfillmentDetailsResponse();
        try {
            response = goldService.findOrderDetails(request);
        } catch (GoldServiceException e) {
            log.error("Exception calling findByOrderId:" + e.toString());
            response.setCode(e.getErrorCode());
            response.setMessage(e.getErrorMsg());
        }
        log.info("findDetails response = " + response);
        return response;
    }

    @Operation(summary = "Fetch invoice link for transaction", description = "200:success,803:SafeGold Invalid Response,805:Safegold connection error,806:Unknown error")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = GetInvoiceResponse.class))})
    @RequestMapping(value = "invoice", method = RequestMethod.POST)
    public GetInvoiceResponse fetchInvoice(@Valid @RequestBody GetInvoiceRequest getInvoiceRequest) {
        GetInvoiceResponse response = new GetInvoiceResponse();
        log.info("Fetch Invoice Request: " + getInvoiceRequest);
        try {
            response = goldService.fetchInvoice(getInvoiceRequest);
        } catch (GoldServiceException e) {
            log.error("Error calling fetchInvoice : " + e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getErrorMsg());
        }
        log.info("Fetch Invoices Response: " + response);
        return response;
    }

    @Operation(summary = "Redrive sell callback", description = "status:1 success else failed")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = GoldSellCallbackResponse.class))})
    @RequestMapping(value = "redrive/callback/{durationInMinutes}", method = RequestMethod.POST)
    public Boolean redriveCallBackTxn(@PathVariable int durationInMinutes) {
        log.info("redriveCallBackTxn :" + durationInMinutes);
        return goldCallbackService.redriveCallBackTxn(durationInMinutes);

    }

    @Operation(summary = "Redrive sell Txn callback", description = "status:1 success else failed")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = GoldSellCallbackResponse.class))})
    @RequestMapping(value = "redrive/callback/order/{order_id}", method = RequestMethod.POST)
    public Boolean redriveCallBackSellTxn(@PathVariable("order_id") String orderId) {
        log.info("redriveCallBackTxn for sell : {}" , orderId);
        return goldCallbackService.redriveCallBackSellTxn(orderId);
    }

    @Operation(summary = "fetch gold balance API by user mobile number", description = "801: Invalid User,803:Safegold Invalid Response,805:Error connecting to SafeGold, please try again")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = FetchBalanceResponse.class))})
    @RequestMapping(value = "user/balance", method = RequestMethod.POST)
    public FetchBalanceResponse fetchBalanceByMobileNumber(@RequestBody GoldBalanceRequest goldBalanceRequest,HttpServletRequest httpServletRequest) {
        FetchBalanceResponse fetchBalanceResponse = new FetchBalanceResponse();
        long startTime =System.currentTimeMillis();
        try {
            fetchBalanceResponse = goldService.fetchBalanceByMobileNumber(goldBalanceRequest.getMobileNumber(),httpServletRequest);
            log.info("Fetch balance response " + fetchBalanceResponse);
            log.info("time taken {}",(System.currentTimeMillis()-startTime));
        } catch (GoldServiceException e) {
            log.error("Error calling fetchBalance : " + e);
            fetchBalanceResponse.setCode(e.getErrorCode());
            fetchBalanceResponse.setMessage(e.getErrorMsg());
        }
        return fetchBalanceResponse;
    }

    @Operation(summary = "fetch live buy price API", description = "200:Success, 805:Safegold connection error")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = BuyPrice.class))})
    @RequestMapping(value = "buy/liveprice", method = RequestMethod.GET)
    public BuyPrice fetchLiveBuyPrice(HttpServletRequest httpRequest) {
        BuyPrice buyPrice = new BuyPrice();
        try {
            buyPrice = goldService.getBuyPrice(GoldConstant.GOLD_PURITY_99);
            Integer minimumBuyPrice = GoldUtils.getMinimumBuyPrice(httpRequest);
            buyPrice.setMinAmount(minimumBuyPrice);
            log.info("fetchBuyLivePrice Response :" + buyPrice);
        } catch (GoldServiceException e) {
            log.error("Error calling fetchBuyLivePrice : " + e);
            buyPrice.setCode(e.getErrorCode());
            buyPrice.setMessage(e.getErrorMsg());
        }
        return buyPrice;
    }


    @Operation(summary = "fetch live sell price API", description = "200:Success, 805:Safegold connection error")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = SellPrice.class))})
    @RequestMapping(value = "sell/liveprice", method = RequestMethod.GET)
    public SellPrice fetchLiveSellPrice() {
        SellPrice sellPrice = new SellPrice();
        try {
            sellPrice = goldService.getSellPrice(GoldConstant.GOLD_PURITY_99);
            log.info("fetchLiveSellPrice Response :" + sellPrice);
        } catch (GoldServiceException e) {
            log.error("Error calling fetchLiveSellPrice : " + e);
            sellPrice.setCode(e.getErrorCode());
            sellPrice.setMessage(e.getErrorMsg());
        }
        return sellPrice;
    }

    @Operation(summary = "fetch gold buy/sell transaction history of loggin user")
    @ApiResponse(content = { @Content(schema = @Schema(implementation = GoldTransactionResponse.class)) })
    @RequestMapping(value = "transaction/history", method = RequestMethod.POST)
    public HashMap<String, Object> fetchGoldTransaction(@RequestBody @Valid GoldBalanceRequest request,HttpServletRequest httpRequest) {
        GoldTransactionResponse transactionResponse = new GoldTransactionResponse();
        HashMap<String, Object> response = new HashMap<>();
        try {
            transactionResponse = goldService.getGoldTransactionHistoryByMobileNumber(request,httpRequest);
            log.info("Gold transaction history response ->{}", transactionResponse);
            response.put(GoldConstant.RESPONSE_MAP_KEY, transactionResponse);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling gold transaction ", e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "Get current investment and investment Returns")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = InvestmentReturnsResponse.class))})
    @RequestMapping(value = "investment/returns", method = RequestMethod.POST)
    public InvestmentReturnsResponse getInvestmentReturns(@Valid @RequestBody InvestmentReturnsRequest request) throws IOException {
        InvestmentReturnsResponse investmentReturnsResponse = null;
        try {
            investmentReturnsResponse =goldService.getCurrentInvestment(request);
            log.info("Investment Returns Response : {}" , investmentReturnsResponse);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling Investment Returns Utils", e);
            investmentReturnsResponse.setCode(e.getErrorCode());
            investmentReturnsResponse.setMessage(e.getMessage());
        }
        return investmentReturnsResponse;
    }


    @Operation(summary = "Fetch investment Details")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = InvestmentDetailsResponse.class))})
    @RequestMapping(value = "investment/details", method = RequestMethod.POST)
    public InvestmentDetailsResponse goldInvestmentDetails(@RequestBody @Valid GMSInvestmentDetailsRequest request) throws GoldServiceException {
        InvestmentDetailsResponse investmentDetailsResponse = new InvestmentDetailsResponse();
        try {
            investmentDetailsResponse = goldService.fetchGMSInvestmentDetails(request);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling investment details ", e);
           investmentDetailsResponse.setCode(e.getErrorCode());
           investmentDetailsResponse.setMessage(e.getMessage());
        }
        catch (IOException e) {
            log.error("Error occur while calling investment details ", e);
            investmentDetailsResponse.setMessage(e.getMessage());
        }
        return investmentDetailsResponse;
    }

    @Operation(summary = "Fetch Delivery Information")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = DeliveryInformationResponse.class))})
    @RequestMapping(value = "delivery/information", method = RequestMethod.POST)
    public DeliveryInformationResponse goldDeliveryInformation(@RequestBody @Valid DeliveryInformationRequest request) throws GoldServiceException {
        DeliveryInformationResponse deliveryResponse = new DeliveryInformationResponse();
        try {
            deliveryResponse = goldService.fetchDeliveryInformation(request);
            log.info("Delivery information response {}", deliveryResponse);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling delivery/information: ", e);
            deliveryResponse.setCode(e.getErrorCode());
            deliveryResponse.setMessage(e.getMessage());
        }
        return deliveryResponse;
    }

    @Operation(summary = "Fetch delivery order details")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = GoldDeliveryInformationResponse.class))})
    @RequestMapping(value = "order/{order_id}", method = RequestMethod.GET)
    public HashMap<String, Object> fetchGoldDeliveryInformation(@PathVariable("order_id") String orderId) {
        GoldDeliveryInformationResponse deliveryInformationResponse = new GoldDeliveryInformationResponse();
       long currentTime  = System.currentTimeMillis();
        HashMap<String, Object> response = new HashMap<>();
        try {
            deliveryInformationResponse = goldService.fetchGoldDeliveryInformation(orderId);
            log.info("Gold Delivery information response {}", deliveryInformationResponse);
            response.put(GoldConstant.RESPONSE_MAP_KEY, deliveryInformationResponse);
            long endTime = System.currentTimeMillis();
            long totalTime =endTime-currentTime;
            log.info("time taken {} ",totalTime);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling order : ", e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "Settle sell transactions in specified date range")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = SellTransactionStatusResponse.class))})
    @RequestMapping(value = "settle/selltransaction", method = RequestMethod.POST)
    public HashMap<String, Object> fetchSellTransactions(@Valid @RequestBody SellTransactionsRequest request) throws GoldServiceException {
        log.info("Fetch gold sell transactions");
        HashMap<String, Object> response = new HashMap<>();
        SellTransactionStatusResponse sellTransactionStatusResponse = new SellTransactionStatusResponse();
        try {
            sellTransactionStatusResponse = goldService.settleSellTransactionBetween(request);
            response.put(GoldConstant.RESPONSE_MAP_KEY, sellTransactionStatusResponse);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling settle/selltransaction  : ", e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "Gold User State")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = GoldUserStateResponse.class))})
    @RequestMapping(value = "user/state", method = RequestMethod.POST)
    public GoldUserStateResponse goldHistory (@RequestBody @Valid GoldUserStateRequest goldUserStateRequest)  {
        GoldUserStateResponse goldUserStateResponse = new GoldUserStateResponse();
        try {
            long startTime = System.currentTimeMillis();
            goldUserStateResponse = goldService.checkUserState(goldUserStateRequest);
            log.info("Time taken {}",(System.currentTimeMillis()-startTime));
            log.info("Gold user state response {}",goldUserStateRequest);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling gold user state api : {}", e);
            goldUserStateResponse.setCode(e.getErrorCode());
            goldUserStateResponse.setMessage(e.getMessage());
        }
        return goldUserStateResponse;
    }
}