package com.freecharge.financial.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface EmailAlertConstant {

	String EMAILS_TO="shrigopal.vishnoi@freecharge.com,vikas.chahal@freecharge.com";
	String EMAIL_FROM="noreply@freecharge.com";
	String EMAIL_REPLY_TO="shrigopal.vishnoi@freecharge.com";
	public static final String SPLIT = ",";

	public static final List<String> BLOCKED_EMAIL_ALERT_ERROR_MSG = new ArrayList<>(Arrays.asList("Invalid User",
			"Gold purchased cannot be sold within 72 hrs",
			"User with given phone no is already registered",
			"user have insufficient balance",
			"User is not registered",
			"user have insufficient balance",
			"Invalid Request Parameter Exception",
			"No Transaction for this order id"));

}
