package com.freecharge.financial.service.communicator;

import com.freecharge.financial.dao.entities.ConfirmTransaction;
import com.freecharge.financial.dto.response.GoldDeliveryInformationResponse;
import com.snapdeal.payments.communicator.response.SendCommunicationIntentResponse;

public interface CommunicatorService {
    public SendCommunicationIntentResponse sendGoldDeliveryCommunication(GoldDeliveryInformationResponse goldDeliveryInformationResponse, String eventType);
    public SendCommunicationIntentResponse sendGoldDeliveryCommunication(GoldDeliveryInformationResponse goldDeliveryInformationResponse, String eventType, ConfirmTransaction confirmTransaction);
}
