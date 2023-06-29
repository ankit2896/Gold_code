package com.freecharge.financial.utility;

import com.freecharge.financial.context.UserContextDirectory;
import com.freecharge.financial.context.UserSessionContext;
import com.freecharge.financial.service.IdentityService;
import com.snapdeal.ims.response.GetUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Http Session Util class used to retrieve the session value from the incoming
 * reuqest
 * 
 * @author fcaa17922
 *
 */
@Component
public class HttpSessionUtils {

	private static final Logger LOG = LoggerFactory.getLogger(HttpSessionUtils.class);

	private static final String APP_SESSION_COOKIE_NAME = "app_fc";

	@Autowired
	private IdentityService imsService;

	public boolean isSessionValid(HttpServletRequest request) {
		String sessionId = HttpRequestUtils.getCookieValue(request, APP_SESSION_COOKIE_NAME);
		LOG.info("sessionId from {} cookie is {} ", APP_SESSION_COOKIE_NAME, sessionId);
		if (sessionId != null) {
			GetUserResponse getUserResponse = imsService.getUserProfileByToken(sessionId);
			LOG.info("getUserResponse : {}",getUserResponse);
			if (getUserResponse != null) {
				String walletId = getUserResponse.getUserDetails().getUserId();
				Integer fcUserId = getUserResponse.getUserDetails().getFcUserId();
				String userEmail = getUserResponse.getUserDetails().getEmailId();
				String imsMobile = getUserResponse.getUserDetails().getMobileNumber();
				StringBuilder userName = new StringBuilder();
				userName.append(getUserResponse.getUserDetails().getFirstName() + " ");
				if(!StringUtils.isEmpty(getUserResponse.getUserDetails().getMiddleName()))
					userName.append(getUserResponse.getUserDetails().getMiddleName()+" ");
				if(!StringUtils.isEmpty(getUserResponse.getUserDetails().getLastName()))
					userName.append(getUserResponse.getUserDetails().getLastName());
				UserSessionContext context = new UserSessionContext();
				context.setFcUserId(fcUserId);
				context.setUserEmail(userEmail);
				context.setWalletId(walletId);
				context.setImsMobile(imsMobile);
				context.setUserName(userName.toString());
				LOG.info("UserSessionContext : {}",context);
				UserContextDirectory.set(context);
				return true;
			}
		}
		return false;
	}

	public String getEmailIDFromRequest() {
		UserSessionContext context = UserContextDirectory.get();
		String emailIDFromFCIms = null;
		if (context != null) {
			emailIDFromFCIms = context.getUserEmail();
		}
		return emailIDFromFCIms;
	}

	public String getWalletIdFromRequest() {
		UserSessionContext context = UserContextDirectory.get();
		String walletId = null;
		if (context != null) {
			walletId = context.getWalletId();
		}
		return walletId;
	}

	public int getUserIDFromRequest() {
		UserSessionContext context = UserContextDirectory.get();
		Integer fcUserId = null;
		if (context != null) {
			fcUserId = context.getFcUserId();
		}
		return fcUserId;
	}

}