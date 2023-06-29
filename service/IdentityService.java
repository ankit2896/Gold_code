package com.freecharge.financial.service;

import com.freecharge.financial.dto.response.UserDetails;
import com.snapdeal.ims.client.IPolicyDocumentUserMappingServiceClient;
import com.snapdeal.ims.client.IUserServiceClient;
import com.snapdeal.ims.client.impl.PolicyDocumentUserMappingServiceClientImpl;
import com.snapdeal.ims.client.impl.UserServiceClientImpl;
import com.snapdeal.ims.dto.UserDetailsDTO;
import com.snapdeal.ims.exception.ServiceException;
import com.snapdeal.ims.request.GetUserByEmailRequest;
import com.snapdeal.ims.request.GetUserByIdRequest;
import com.snapdeal.ims.request.GetUserByMobileRequest;
import com.snapdeal.ims.request.GetUserByTokenRequest;
import com.snapdeal.ims.response.GetUserResponse;
import com.snapdeal.ims.utils.ClientDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class IdentityService {

	@Value("${ims.client.url}")
	private String clientURL;
	@Value("${ims.client.id}")
	private String clientId;
	@Value("${ims.client.key}")
	private String clientKey;
	@Value("${ims.client.port}")
	private String clientPort;
	@Value("${ims.slave.client.url}")
	private String slaveUrl;
	@Value("${ims.slave.client.port}")
	private String slavePort;
	@Value("${ims.client.timeout}")
	private Integer timeout;

	private IUserServiceClient userClient;

	@PostConstruct
	public void init() throws Exception {
		ClientDetails.init(clientURL, clientPort,
				slaveUrl, slavePort,
				clientKey, clientId, timeout);
		userClient = new UserServiceClientImpl();
	}

	@Bean
	public IPolicyDocumentUserMappingServiceClient getImsClient(){
		return new PolicyDocumentUserMappingServiceClientImpl();
	}

	public UserDetailsDTO getOneCheckUserByEmail(String email) {
		if (email == null) {
			return null;
		}
		try {
			GetUserByEmailRequest request = new GetUserByEmailRequest();
			request.setEmailId(email);
			log.info("Sending request with email " + email);
			GetUserResponse userByToken = userClient.getUserByEmail(request);
			log.info("Got response with email " + email);
			return userByToken.getUserDetails();
		} catch (ServiceException e) {
			log.error("ServiceException while getting user from email: " + email, e);
			return null;
		} catch (Exception e) {
			log.error("Exception while getting user from email: " + email, e);
			return null;
		}
	}

	public GetUserResponse getUserProfileByToken(String sessionId) {
		GetUserByTokenRequest request = new GetUserByTokenRequest();
		request.setToken(sessionId);
		try {
			GetUserResponse response = userClient.getUserByToken(request);
			log.info("getUserProfileByToken: " + response.toString());
			return response;
		} catch (Exception e) {
			log.error("While fetching user details for token" + sessionId, e);
		}
		return null;
	}

	public UserDetailsDTO getUserById(String userId) {
		if (StringUtils.isEmpty(userId)) {
			return null;
		}
		try {
			GetUserByIdRequest request = new GetUserByIdRequest();
			request.setUserId(userId);
			log.info("Get user by id request {}", request);
			GetUserResponse userById = userClient.getUserById(request);
			log.info("Get user by id response {}", userById);
			return userById.getUserDetails();
		} catch (ServiceException e) {
			log.error("ServiceException while getting user from userId: {}===={}", userId, e);
			return null;
		}

	}

	public UserDetailsDTO getOneCheckUserByUserId(String userId) {
		if (userId == null) {
			return null;
		}
		try {
			GetUserByIdRequest request = new GetUserByIdRequest();
			request.setUserId(userId);
			log.info("Sending request with userId {} " , userId);
			long startTime = System.currentTimeMillis();
			GetUserResponse userByToken = userClient.getUserById(request);
			log.info("GetUserResponse {} " , userByToken);
			long endTime = System.currentTimeMillis();
			log.info("Got response with email " + userId + " : in " + (endTime - startTime) + " m.s");
			return userByToken.getUserDetails();
		} catch (ServiceException e) {
			log.error("ServiceException while getting user from userId: " + userId, e);
			return null;
		} catch (Exception e) {
			log.error("Exception while getting user from userId: " + userId, e);
			return null;
		}
	}
	public String getImsIDByPhoneNo(String userPhoneNo) {
		if (userPhoneNo == null) {
			return null;
		}
		try {
			GetUserByMobileRequest request = new GetUserByMobileRequest();
			request.setMobileNumber(userPhoneNo);
			log.info("Sending request with userPhoneNo: {} " , userPhoneNo);
			long startTime = System.currentTimeMillis();
			GetUserResponse userByToken = userClient.getUserByVerifiedMobile(request);
			//log.info("GetUserResponse {} " , userByToken);
			long endTime = System.currentTimeMillis();
			log.info("Got response with phone no " + userPhoneNo + " : in " + (endTime - startTime) + " m.s");
			return userByToken.getUserDetails().getUserId();
		} catch (ServiceException e) {
			log.error("ServiceException while getting user from userPhoneNo: " + userPhoneNo, e);
			return null;
		} catch (Exception e) {
			log.error("Exception while getting user from userPhoneNo: " + userPhoneNo, e);
			return null;
		}
	}

	public CompletableFuture<UserDetails> getUserDetailsByPhoneNo(String phoneNo) {
		return CompletableFuture.supplyAsync(() -> {
			UserDetails user = new UserDetails();
			UserDetailsDTO userDto = getUserByPhoneNo(phoneNo);
			if (Objects.nonNull(user)) {
				user.setEmail(userDto.getEmailId());
				user.setName(userDto.getFirstName());
				user.setMobile(userDto.getMobileNumber());
			}
			return user;
		});
	}
	public UserDetailsDTO getUserByPhoneNo(String userPhoneNo) {
		if (userPhoneNo == null) {
			return null;
		}
		try {
			GetUserByMobileRequest request = new GetUserByMobileRequest();
			request.setMobileNumber(userPhoneNo);
			log.info("Sending request with userId {} " , userPhoneNo);
			long startTime = System.currentTimeMillis();
			GetUserResponse userByToken = userClient.getUserByVerifiedMobile(request);
			log.info("GetUserResponse {} " , userByToken);
			long endTime = System.currentTimeMillis();
			log.info("Got response with email " + userPhoneNo + " : in " + (endTime - startTime) + " m.s");
			return userByToken.getUserDetails();
		} catch (ServiceException e) {
			log.error("ServiceException while getting user from userId: " + userPhoneNo, e);
			return null;
		} catch (Exception e) {
			log.error("Exception while getting user from userId: " + userPhoneNo, e);
			return null;
		}
	}
    public String getImsIdByPhoneNumber(String userPhoneNo) {
		UserDetailsDTO user = getUserByPhoneNo(userPhoneNo);
		log.info("IMS user ->{}", user);
		return Objects.isNull(user) ? null : user.getUserId();
	}

}
