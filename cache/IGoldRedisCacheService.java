package com.freecharge.financial.cache;

import com.freecharge.financial.dto.response.ProductDetailsResponse;

public interface IGoldRedisCacheService {

	String getGraphDataCache(String url);

	String putGraphDataCache(String url, String response);

	String getFaqCache(String url);

	String putFaqCache(String url,  String response);

	public String getUpmTriggerCountCache(String key);

	public String putUpmTriggerCountCache(String key, String count);

	public String getUserRegisterCountPage(String key);

	public String putUserRegisterPageCountCache(String key, String count);

	public ProductDetailsResponse getProductDetails(String key);

	public ProductDetailsResponse putProductDetails(String key, ProductDetailsResponse response);

	public void flushProductDetails(String key);
}
