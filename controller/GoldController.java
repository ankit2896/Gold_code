package com.freecharge.financial.controller;

import com.freecharge.financial.constants.GoldConstant;
import com.freecharge.financial.context.UserContextDirectory;
import com.freecharge.financial.dto.request.*;
import com.freecharge.financial.dto.response.*;
import com.freecharge.financial.exception.GoldServiceException;
import com.freecharge.financial.service.GoldService;
import com.freecharge.financial.service.IUtilityService;
import com.freecharge.financial.utility.GoldUtils;
import com.google.gson.Gson;
import com.snapdeal.payments.disbursement.model.PennyDropResponse;
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
@RequestMapping("/gold/api/")
@Tag(name = "Gold Homepage Controller",description = "Gold Homepage Controller")
public class GoldController {
    
    @Autowired
    private GoldService goldService;

    @Autowired
    private IUtilityService iUtilityService;

    @Operation(summary = "fetch gold balance API")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = FetchBalanceResponse.class))})
    @RequestMapping(value = "fetch/balance", method = RequestMethod.GET)
    public HashMap<String, Object> fetchBalance(HttpServletRequest httpRequest) {
        FetchBalanceResponse fetchBalanceResponse = new FetchBalanceResponse();
        HashMap<String, Object>  response = new HashMap<>();
        long startTime =System.currentTimeMillis();
        try {
            fetchBalanceResponse = goldService.fetchBalance(true,httpRequest);
            log.info("Fetch balance response " + fetchBalanceResponse);
            response.put(GoldConstant.RESPONSE_MAP_KEY, fetchBalanceResponse);
            log.info("time taken {}",(System.currentTimeMillis()-startTime));
        } catch (GoldServiceException e) {
            log.error("Error calling fetchBalance : " + e);
            iUtilityService.sendEmailAlerts("Error calling fetchBalance",GoldUtils.getRequestMetdata("fetch/balance",null,null),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "fetch gold balance API for returning user of Gold dev")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = ReturningUserDelivery.class))})
    @RequestMapping(value = "v2/fetch/balance", method = RequestMethod.GET)
    public HashMap<String, Object> fetchBalanceForReturningUser(HttpServletRequest httpRequest) {
        HashMap<String, Object> response = new HashMap<>();
        long startTime = System.currentTimeMillis();
        try {
            ReturningUserDelivery returningUserDelivery = goldService.fetchBalanceForReturningUser(httpRequest);
            log.info("Fetch balance response " + returningUserDelivery);
            response.put(GoldConstant.RESPONSE_MAP_KEY, returningUserDelivery);
            log.info("time taken {}",(System.currentTimeMillis()-startTime));
        } catch (GoldServiceException e) {
            log.error("Error calling v2 fetchBalance : " + e);
            iUtilityService.sendEmailAlerts("Error calling fetchBalance",
                    GoldUtils.getRequestMetdata("v2/fetch/balance", null, null), e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }


    @Operation(summary = "fetch gold balance API by userId")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = FetchBalanceResponse.class))})
    @RequestMapping(value = "user/balance", method = RequestMethod.POST)
    public HashMap<String, Object> fetchBalanceByUserId(@RequestBody GoldBalanceRequest goldBalanceRequest,HttpServletRequest httpRequest) {
        FetchBalanceResponse fetchBalanceResponse = new FetchBalanceResponse();
        HashMap<String, Object>  response = new HashMap<>();
        long startTime = System.currentTimeMillis();
        try {
            fetchBalanceResponse = goldService.fetchBalanceByMobileNumber(goldBalanceRequest.getMobileNumber(), httpRequest);
            log.info("Fetch balance response " + fetchBalanceResponse);
            response.put(GoldConstant.RESPONSE_MAP_KEY, fetchBalanceResponse);
            log.info("time taken {}",(System.currentTimeMillis()-startTime));
        } catch (GoldServiceException e) {
            log.error("Error calling fetchBalance : " + e);
            iUtilityService.sendEmailAlerts("Error calling fetchBalance",GoldUtils.getRequestMetdata("user/balance",null,goldBalanceRequest.toString()),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "register user with safegold API")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = RegisterUserResponse.class))})
    @RequestMapping(value = "user/register", method = RequestMethod.POST)
    public HashMap<String, Object> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        log.info("Register User Request: " + registerUserRequest);
        RegisterUserResponse registerUserResponse = new RegisterUserResponse();
        HashMap<String, Object> response = new HashMap<>();
        try {
            registerUserResponse = goldService.registerUser(registerUserRequest);
            response.put(GoldConstant.RESPONSE_MAP_KEY, registerUserResponse);
        } catch (GoldServiceException e) {
            log.error("Error calling registerUser : " + e);
            log.error("Error calling registerUser : " + new Gson().toJson(e));
            log.error("Error calling registerUser : " + e.getMessage());
           // iUtilityService.sendEmailAlerts("Error calling registerUser",GoldUtils.getRequestMetdata("user/register",null,registerUserRequest.toString()),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        log.info("Register User Response: " + response);
        return response;
    }

    @Operation(summary = "fetch live buy price API")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = BuyPrice.class))})
    @RequestMapping(value = "buy/liveprice", method = RequestMethod.GET)
    public HashMap<String, Object> fetchLiveBuyPrice(HttpServletRequest httpRequest) {
        BuyPrice buyPrice = new BuyPrice();
        HashMap<String, Object> response = new HashMap<>();
        try {
            buyPrice = goldService.getBuyPrice(GoldConstant.GOLD_PURITY_99);
            Integer minimumBuyPrice = GoldUtils.getMinimumBuyPrice(httpRequest);
            buyPrice.setMinAmount(minimumBuyPrice);
            response.put(GoldConstant.RESPONSE_MAP_KEY, buyPrice);
            log.info("fetchBuyLivePrice Response :" + buyPrice);
        } catch (GoldServiceException e) {
            log.error("Error calling fetchBuyLivePrice : " + e);
            iUtilityService.sendEmailAlerts("Error calling fetchBuyLivePrice",GoldUtils.getRequestMetdata("buy/liveprice",null,null),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }


    @Operation(summary = "fetch live sell price API")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = SellPrice.class))})
    @RequestMapping(value = "sell/liveprice", method = RequestMethod.GET)
    public HashMap<String, Object> fetchLiveSellPrice() {
        SellPrice sellPrice = new SellPrice();
        HashMap<String, Object> response = new HashMap<>();
        try {
            sellPrice = goldService.getSellPrice(GoldConstant.GOLD_PURITY_99);
            response.put(GoldConstant.RESPONSE_MAP_KEY, sellPrice);
            log.info("fetchLiveSellPrice Response :" + sellPrice);
        } catch (GoldServiceException e) {
            log.error("Error calling fetchLiveSellPrice : " + e);
            iUtilityService.sendEmailAlerts("Error calling fetchLiveSellPrice",GoldUtils.getRequestMetdata("sell/liveprice",null,null),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "fetch live buy price API", description = "200:Success, 805:Safegold connection error")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = BuyPrice.class))})
    @RequestMapping(value = "v2/buy/liveprice", method = RequestMethod.GET)
    public HashMap<String, Object> fetchLiveBuyPriceV2(HttpServletRequest httpRequest) {
        BuyPrice buyPrice = new BuyPrice();
        HashMap<String, Object> response = new HashMap<>();
        try {
            buyPrice = goldService.getBuyPrice(GoldConstant.GOLD_PURITY_99);
            Integer minimumBuyPrice = GoldUtils.getMinimumBuyPrice(httpRequest);
            buyPrice.setMinAmount(minimumBuyPrice);
            response.put(GoldConstant.RESPONSE_MAP_KEY, buyPrice);
            log.info("fetchBuyLivePrice Response :" + buyPrice);
        } catch (GoldServiceException e) {
            log.error("Error calling fetchBuyLivePrice : " + e);
            iUtilityService.sendEmailAlerts("Error calling fetchBuyLivePrice :", GoldUtils.getRequestMetdata("v2/buy/liveprice", null, null), e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "fetch live sell price API", description = "200:Success, 805:Safegold connection error")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = SellPrice.class))})
    @RequestMapping(value = "v2/sell/liveprice", method = RequestMethod.GET)
    public HashMap<String, Object> fetchLiveSellPriceV2() {
        SellPrice sellPrice = new SellPrice();
        HashMap<String, Object> response = new HashMap<>();
        try {
            sellPrice = goldService.getSellPrice(GoldConstant.GOLD_PURITY_99);
            response.put(GoldConstant.RESPONSE_MAP_KEY, sellPrice);
            log.info("fetchLiveSellPrice Response :" + sellPrice);
        } catch (GoldServiceException e) {
            log.error("Error calling fetchLiveSellPrice : " + e);
            iUtilityService.sendEmailAlerts("Error calling fetchLiveSellPrice :", GoldUtils.getRequestMetdata("v2/sell/liveprice", null, null), e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);

        }
        return response;
    }

    @Operation(summary = "block buy/sell transaction")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = VerifyPriceResponse.class))})
    @RequestMapping(value = "transaction/block", method = RequestMethod.POST)
    public HashMap<String, Object> blockSellTransaction(@RequestBody @Valid VeriyPriceRequest priceRequest, HttpServletRequest httpServletRequest) {
        VerifyPriceResponse priceResponse = new VerifyPriceResponse();
        HashMap<String, Object> response = new HashMap<>();
        try {
            String imsUserId = UserContextDirectory.get().getWalletId();
//            imsUserId="VjAxI2ZlOTUzZjZhLWNmZTctNDc4OC1hMDhkLWI1ZjgxMzQ3YmQ3YQ";
            goldService.recordPolicy(imsUserId);
            GoldUtils.validateBlock(priceRequest, httpServletRequest);
            priceResponse = goldService.verifyGoldPrice(priceRequest,httpServletRequest);
            response.put(GoldConstant.RESPONSE_MAP_KEY, priceResponse);
            log.info("blockTransaction Response :" + priceResponse);
        } catch (GoldServiceException e) {
            log.error("Error calling blockTransaction : " + e);
            //iUtilityService.sendEmailAlerts("Error calling blockTransaction",GoldUtils.getRequestMetdata("block buy/sell transaction",null,priceRequest.toString()),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "confirm transaction or status check")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = PaymentResponse.class))})
    @RequestMapping(value = "transaction/confirm", method = RequestMethod.POST)
    public PaymentResponse checkTransactionStatus(@RequestBody PaymentRequest priceRequest) {
        PaymentResponse paymentResponse = new PaymentResponse();
        try {
            paymentResponse = goldService.checkPaymentStatus(priceRequest);
            log.info("payment Response :" + paymentResponse);
        } catch (GoldServiceException e) {
            log.error("Error calling checkTransaction : " + e);
            iUtilityService.sendEmailAlerts("Error calling transaction/confirm",GoldUtils.getRequestMetdata("transaction/confirm",null,priceRequest.toString()),e);
            paymentResponse.setCode(e.getErrorCode());
            paymentResponse.setMessage(e.getErrorMsg());
        }
        return paymentResponse;
    }

    @Operation(summary = "Lookup in block transaction for lookupId")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = BlockTransactionResponse.class))})
    @RequestMapping(value = "transaction/data", method = RequestMethod.POST)
    public BlockTransactionResponse getTransactionResponse(@RequestBody BlockTransactionRequest transactionRequest) {
        BlockTransactionResponse transactionResponse = new BlockTransactionResponse();
        try {
            transactionResponse = goldService.getTransactionResponse(transactionRequest);
        } catch (GoldServiceException e) {
            log.error("Exception occur while calling transaction/data API :{}",transactionRequest);
            iUtilityService.sendEmailAlerts("Error calling transaction/data",GoldUtils.getRequestMetdata("transaction/data",null,transactionRequest.toString()),e);
            transactionResponse.setCode(e.getErrorCode());
            transactionResponse.setMessage(e.getErrorMsg());

        }
        log.info("transaction/data API response :{}",transactionRequest);
        return transactionResponse;
    }

    public GoldOrderStatusResponse orderStatus(@RequestBody GoldOrderStatusRequest orderStatusRequest) {
        return null;
    }

    @Operation(summary = "Publish Bank Details for sell transactions")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = PublishBankDetailsResponse.class))})
    @RequestMapping(value = "sell/postbankdetails", method = RequestMethod.POST)
    public HashMap<String, Object> publishBankDetails(@RequestBody PublishBankDetailsRequest publishBankDetailsRequest) {
        PublishBankDetailsResponse bankDetailsResponse = new PublishBankDetailsResponse();
        HashMap<String, Object> response = new HashMap<>();
        log.info("Publish Bank Details Request: " + publishBankDetailsRequest);
        try {
            bankDetailsResponse = goldService.publishBankDetails(publishBankDetailsRequest);
            response.put(GoldConstant.RESPONSE_MAP_KEY, bankDetailsResponse);
        } catch (GoldServiceException e) {
            log.error("Error calling publishBankDetails : " + e);
            iUtilityService.sendEmailAlerts("Error calling publishBankDetails",GoldUtils.getRequestMetdata("sell/postbankdetails",null,publishBankDetailsRequest.toString()),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        log.info("Publish Bank Details Response: " + response);
        return response;
    }

    @Operation(summary = "Publish Bank Details for sell transactions V2")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = PublishBankDetailsResponse.class))})
    @RequestMapping(value = "v2/sell/postbankdetails", method = RequestMethod.POST)
    public HashMap<String, Object> publishBankDetailsV2(@RequestBody PublishBankDetailsRequestV2 request) {
        PublishBankDetailsResponse bankDetailsResponse = new PublishBankDetailsResponse();
        HashMap<String, Object> response = new HashMap<>();
        log.info("Publish bank details request: " + request);
        try {
            bankDetailsResponse = goldService.publishBankDetailsV2(request);
            response.put(GoldConstant.RESPONSE_MAP_KEY, bankDetailsResponse);
        } catch (GoldServiceException e) {
            log.error("Error calling publishBankDetails : " + e);
            iUtilityService.sendEmailAlerts("Error calling publishBankDetails V2"
                    ,GoldUtils.getRequestMetdata("v2/sell/postbankdetails",null,request.toString()),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        log.info("Publish Bank Details Response: " + response);
        return response;
    }

    @Operation(summary = "Fetch invoice link for transaction")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = GetInvoiceResponse.class))})
    @RequestMapping(value = "invoice", method = RequestMethod.POST)
    public GetInvoiceResponse fetchInvoice(@Valid @RequestBody GetInvoiceRequest getInvoiceRequest) {
        GetInvoiceResponse response = new GetInvoiceResponse();
        log.info("Fetch Invoice Request: " + getInvoiceRequest);
        try {
            response = goldService.fetchInvoice(getInvoiceRequest);
        } catch (GoldServiceException e) {
            log.error("Error calling fetchInvoice : " + e);
            iUtilityService.sendEmailAlerts("Error calling fetchInvoice",GoldUtils.getRequestMetdata("invoice",null,getInvoiceRequest.toString()),e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getErrorMsg());
        }
        log.info("Fetch Invoices Response: " + response);
        return response;
    }

    @Operation(summary = "Check if bank details of user are present")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = CheckBankDetailsResponse.class))})
    @RequestMapping(value = "sell/checkbankdetails", method = RequestMethod.GET)
    public HashMap<String, Object> checkBankDetails() {
        log.info("Calling checkBankDetails");
        HashMap<String, Object>  response = new HashMap<String, Object>();
        try {
            CheckBankDetailsResponse checkBankDetailsResponse = goldService.checkBankDetails();
            response.put(GoldConstant.RESPONSE_MAP_KEY, checkBankDetailsResponse);
        } catch (GoldServiceException e) {
            log.error("Exception calling checkBankDetails: " + e.toString());
            iUtilityService.sendEmailAlerts("Exception calling checkBankDetails",GoldUtils.getRequestMetdata("sell/checkbankdetails",null,null),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        log.info("Check Bank Details Response: " + response);
        return response;
    }

    @Operation(summary = "Return fulfillment details")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = FulfillmentDetailsResponse.class))})
    @RequestMapping(value = "fulfillment/details", method = RequestMethod.POST)
    public HashMap<String, Object> findDetails(@RequestBody FulfillmentDetailsRequest request,HttpServletRequest httpServletRequest) {
        log.info("Calling findDetails");
        FulfillmentDetailsResponse fulfillmentDetailsResponse = new FulfillmentDetailsResponse();
        HashMap<String, Object>  response = new HashMap<String, Object>();
        try {
            fulfillmentDetailsResponse = goldService.findDetails(request,httpServletRequest);
            response.put(GoldConstant.RESPONSE_MAP_KEY, fulfillmentDetailsResponse);
        } catch (GoldServiceException e) {
            log.error("Exception calling findByOrderId:" + e.toString());
            iUtilityService.sendEmailAlerts("Exception calling fulfillment details",GoldUtils.getRequestMetdata("fulfillment/details",null,request.toString()),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        log.info("findDetails response = " + response);
        return response;
    }


    @Operation(summary = "Verify Phone updated")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = VerifyPhoneUpdateResponse.class))})
    @RequestMapping(value = "verify/phoneupdate", method = RequestMethod.POST)
    public HashMap<String, Object> verifyPhoneUpdate(@RequestBody VerifyPhoneUpdateRequest request) {
        VerifyPhoneUpdateResponse verifyPhoneUpdateResponse = new VerifyPhoneUpdateResponse();
        log.info("VerifyPhoneupdate Request: " + request);
        HashMap<String, Object>  response = new HashMap<>();

        try {
            verifyPhoneUpdateResponse = goldService.verifyPhoneUpdate(request.getMobileNumber());
            response.put(GoldConstant.RESPONSE_MAP_KEY, verifyPhoneUpdateResponse);
        } catch (GoldServiceException e) {
            log.error("Error calling VerifyPhoneupdate : " + e);
            iUtilityService.sendEmailAlerts("Error calling VerifyPhoneupdate",GoldUtils.getRequestMetdata("fulfillment/details",null,request.toString()),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        log.info("VerifyPhoneupdate Response: " + response);
        return response;
    }

    @Operation(summary = "Validate pincode for gold delivery")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = ValidatePincodeResponse.class))})
    @RequestMapping(value = "validate/pincode/{pincode}", method = RequestMethod.POST)
    public HashMap<String,Object> pincodeValidate(@PathVariable("pincode") String pincode){
        HashMap<String,Object> response = new HashMap<>();
        try{
            ValidatePincodeResponse validatePinCodeResponse = goldService.validatePinCode(pincode);
            response.put(GoldConstant.RESPONSE_MAP_KEY, validatePinCodeResponse);
            log.info("validate pincode response = " + validatePinCodeResponse);
        }catch(GoldServiceException e){
            log.error("Exception calling validate pincode:" + e.toString());
            iUtilityService.sendEmailAlerts("Exception calling validate pincode",GoldUtils.getRequestMetdata("validate/pincode/{pincode}",null,pincode),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }

        return response;
    }

    @Operation(summary = "Return Product list")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = ProductDetailsResponse.class))})
    @RequestMapping(value = "redeem/productlist", method = RequestMethod.POST)
    public HashMap<String, Object> productList(@RequestBody(required = false) RedeemRequest request) {
        log.info("Calling product list:- "+request);
        HashMap<String, Object>  response = new HashMap<>();
        try {
            ProductDetailsResponse productDetailsResponse = goldService.getRedeemProduct(request);
            response.put(GoldConstant.RESPONSE_MAP_KEY, productDetailsResponse);
        } catch (GoldServiceException e) {
            log.error("Exception calling product list:" + e.toString());
            iUtilityService.sendEmailAlerts("Exception calling product list",GoldUtils.getRequestMetdata("redeem/productlist",null,request.toString()),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }


    @Operation(summary = "fetch all address of user API")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = AddressDetailsResponse.class))})
    @RequestMapping(value = "fetch/all-address", method = RequestMethod.GET)
    public HashMap<String, Object> fetchAllAddress() {
        AddressDetailsResponse addressDetailsResponse;
        HashMap<String, Object>  response = new HashMap<>();
        try {
            addressDetailsResponse = goldService.fetchAllUserAddress();
            log.info("Fetch user address response " + addressDetailsResponse);
            response.put(GoldConstant.RESPONSE_MAP_KEY, addressDetailsResponse);
        } catch (GoldServiceException e) {
            log.error("Error calling fetch/all-address : " + e);
            iUtilityService.sendEmailAlerts("Error calling fetch/all-address",GoldUtils.getRequestMetdata("fetch/all-address",null,null),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }


    @Operation(summary = "fetch dispatch order status of the gold redeem transaction")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = RedeemGoldDispatchStatusResponse.class))})
    @RequestMapping(value = "redeem-gold/dispatch-status/{order_id}", method = RequestMethod.GET)
    public HashMap<String, Object> redeemDispatchStatus(@PathVariable("order_id") String orderId){
        Object redeemGoldDispatchStatusResponse;
        HashMap<String, Object>  response = new HashMap<>();
        try {
            redeemGoldDispatchStatusResponse = goldService.redeemGoldDispatchStatus(orderId);
            log.info("Redeem gold order dispatch status response " + redeemGoldDispatchStatusResponse);
            response.put(GoldConstant.RESPONSE_MAP_KEY, redeemGoldDispatchStatusResponse);
        } catch (GoldServiceException e) {
            log.error("Error Redeem gold order dispatch status : " + e);
            iUtilityService.sendEmailAlerts("Error Redeem gold order dispatch status",GoldUtils.getRequestMetdata("redeem-gold/dispatch-status/"+orderId,null,null),e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

	@Operation(summary = "fetch gold buy/sell transaction history of loggin user")
	@ApiResponse(content = { @Content(schema = @Schema(implementation = GoldTransactionResponse.class)) })
	@RequestMapping(value = "transaction/history", method = RequestMethod.POST)
	public HashMap<String, Object> fetchGoldTransaction(@RequestBody(required = false) TransactionRequest request,HttpServletRequest httpRequest) {
		GoldTransactionResponse transactionResponse = new GoldTransactionResponse();
		HashMap<String, Object> response = new HashMap<>();
		try {
			transactionResponse = goldService.getGoldTransactionHistory(request, httpRequest);
			//log.info("Gold transaction history response ->{}", transactionResponse);
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

    @Operation(summary = "fetch Graph Data")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = GraphDataResponse.class))})
    @RequestMapping(value = "graph/{type}/{period}", method = RequestMethod.GET)
    public HashMap<String, Object> fetchGraphData(@PathVariable String type, @PathVariable String period) throws IOException {
        GraphDataResponse graphResponse;
        HashMap<String, Object> response = new HashMap<>();
        try {
            graphResponse = goldService.getGraphData(type, period);
           // log.info("Gold graph response ->{}", graphResponse);
            response.put(GoldConstant.RESPONSE_MAP_KEY, graphResponse);
        } catch (GoldServiceException e) {
            log.error("Error occur while fetching data from graph ", e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "Fetch bank details of users")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = BankDetailResponse.class))})
    @RequestMapping(value = "bankdetails", method = RequestMethod.GET)
    public HashMap<String, Object> getBankDetails(@RequestParam(required = false) String imsUserId) {
        BankDetailResponse bankDetailResponse;
        HashMap<String, Object> response = new HashMap<>();
        try {
            bankDetailResponse = goldService.getBankDetails(imsUserId);
            log.info("Bank details response ->{}", bankDetailResponse);
            response.put(GoldConstant.RESPONSE_MAP_KEY, bankDetailResponse);
        } catch (GoldServiceException e) {
            log.error("Error occur while fetching data from banks details ", e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            iUtilityService.sendEmailAlerts("Error occur while fetching data from banks details", GoldUtils.getRequestMetdata("/bankdetails", "imsuserId : " + imsUserId, null), e);
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "Fetch FAQ of Digital Gold")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = FaqResponse.class))})
    @RequestMapping(value = "faq", method = RequestMethod.GET)
    public HashMap<String, Object> getFaq() throws GoldServiceException {
        FaqResponse faqResponse;
        HashMap<String, Object> response = new HashMap<>();
        try {
            faqResponse = goldService.getFaq();
            response.put(GoldConstant.RESPONSE_MAP_KEY, faqResponse);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling FAQ", e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            iUtilityService.sendEmailAlerts("Error occur while calling FAQ", e);
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "Validate bank details from penny drop")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = PennyDropResponse.class))})
    @RequestMapping(value = "pennydrop", method = RequestMethod.POST)
    public HashMap<String, Object> pennydrop(@RequestBody PennyDropRequestGC request) throws GoldServiceException {
        PennyDropResponse pennyDropResponse;
        HashMap<String, Object> response = new HashMap<>();
        try {
            pennyDropResponse = goldService.pennydrop(request);
            response.put(GoldConstant.RESPONSE_MAP_KEY, pennyDropResponse);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling pennydrop API", e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "Digital gold banner")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = GoldBannerResponse.class))})
    @RequestMapping(value = "banner", method = RequestMethod.GET)
    public HashMap<String, Object> goldBanner() throws GoldServiceException {
        GoldBannerResponse goldBannerResponse;
        HashMap<String, Object> response = new HashMap<>();
        try {
            goldBannerResponse = goldService.getGoldBanner();
            response.put(GoldConstant.RESPONSE_MAP_KEY, goldBannerResponse);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling Gold banner", e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            iUtilityService.sendEmailAlerts("Error occur while calling Gold banner", e);
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

    @Operation(summary = "Add or update delivery address of gold")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = AddressDetailsResponse.class))})
    @RequestMapping(value = "delivery/address",method = RequestMethod.POST)
    public HashMap<String,Object> addOrUpdateAddress(@RequestBody @Valid RequestAddress requestAddress) throws GoldServiceException {
        HashMap<String, Object> response = new HashMap<>();
        try {
            AddressDetailsResponse address = goldService.addOrUpdateAddress(requestAddress);
            response.put(GoldConstant.RESPONSE_MAP_KEY, address);
            log.info("Delivery address response : " + address);
        } catch (GoldServiceException e) {
            log.error("Error calling confirm address : " + e);
            ErrorResponse error = new ErrorResponse();
            error.setErrorCode(e.getErrorCode());
            error.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, error);
        }
        return response;
    }

    @Operation(summary = "Delete the delivery address of gold")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = AddressDetailsResponse.class))})
    @RequestMapping(value = "delete/{address_id}/address",method = RequestMethod.DELETE)
    public HashMap<String,Object> deleteAddress(@PathVariable("address_id") String addressId) throws GoldServiceException {
        HashMap<String, Object> response = new HashMap<>();
        try {
            AddressDetailsResponse address = goldService.deleteAddress(addressId);
            response.put(GoldConstant.RESPONSE_MAP_KEY, address);
            log.info("Delivery address response : " + address);
        } catch (GoldServiceException e) {
            log.error("Error calling confirm address : " + e);
            ErrorResponse error = new ErrorResponse();
            error.setErrorCode(e.getErrorCode());
            error.setErrorMessage(e.getErrorMsg());
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, error);
        }
        return response;
    }

    @Operation(summary = "User KYC")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = UserKYCResponse.class))})
    @RequestMapping(value = "/user/kyc", method = RequestMethod.POST)
    public HashMap<String , Object> completeUserKYC (@RequestBody @Valid UserKYCRequest request) throws GoldServiceException {
        HashMap<String, Object> response = new HashMap<>();
        UserKYCResponse userKYCResponse = new UserKYCResponse();
        try {
            userKYCResponse = goldService.completeUserKYC(request);
            response.put(GoldConstant.RESPONSE_MAP_KEY, userKYCResponse);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling user kyc api : {}", e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            iUtilityService.sendEmailAlerts("Error occur while calling user kyc", e);
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }
    @Operation(summary = "Get User Pan Details")
    @ApiResponse(content = {@Content(schema = @Schema(implementation = UserPanDetailsResponse.class))})
    @RequestMapping(value = "/panDetails", method = RequestMethod.GET)
    public HashMap<String , Object> getUserPanDetails () throws GoldServiceException  {
        HashMap<String, Object> response = new HashMap<>();
       UserPanDetailsResponse userPanDetailsResponse = new UserPanDetailsResponse();
        try {
            userPanDetailsResponse = goldService.getUserPanDetails();
            response.put(GoldConstant.RESPONSE_MAP_KEY, userPanDetailsResponse);
        } catch (GoldServiceException e) {
            log.error("Error occur while calling user get Pan Details api : {}", e);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(e.getErrorCode());
            errorResponse.setErrorMessage(e.getErrorMsg());
            iUtilityService.sendEmailAlerts("Error occur while calling user get Pan Details ", e);
            response.put(GoldConstant.RESPONSE_MAP_ERROR_KEY, errorResponse);
        }
        return response;
    }

}

