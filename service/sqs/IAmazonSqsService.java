package com.freecharge.financial.service.sqs;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageResult;

public interface IAmazonSqsService {

    Message getMessageFromQueue(String queueUrl);

    void deleteMessageFromQueue(Message message, String queueUrl);

    SendMessageResult sendMessageToQueue(String message, String queueURL);
}
