package com.freecharge.financial.service;


import com.freecharge.financial.dto.request.RequestAddress;
import com.freecharge.financial.dto.response.AddressDetailsResponse;
import com.freecharge.financial.exception.GoldServiceException;
import com.freecharge.ups.requests.DeleteAddressRequest;
import com.freecharge.ups.requests.GetAddressIdRequest;
import com.freecharge.ups.response.UserAddressResponse;

import javax.validation.Valid;
import java.util.List;

public interface UPSService {
    AddressDetailsResponse addAddress(RequestAddress request) throws GoldServiceException;

    AddressDetailsResponse updateAddress(RequestAddress request) throws GoldServiceException;

    AddressDetailsResponse deleteAddress(DeleteAddressRequest request) throws GoldServiceException;

    AddressDetailsResponse fetchAllAddressByUserId(String imsUser) throws GoldServiceException;

    List<UserAddressResponse> getAddressByUserId(String imsUser) throws GoldServiceException;

    UserAddressResponse getAddressByAddressId(@Valid GetAddressIdRequest request) throws GoldServiceException;

    AddressDetailsResponse addressResponseParser(List<UserAddressResponse> addressResponseList) throws GoldServiceException;

    }
