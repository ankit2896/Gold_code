package com.freecharge.financial.service;

import com.freecharge.financial.dto.request.EmailBusinessDO;

public interface IEmailSender {
    void sendEmail(EmailBusinessDO emailBusinessDO);
}
