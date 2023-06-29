package com.freecharge.financial.cache;

import com.freecharge.financial.constants.GoldCacheConstants;
import com.freecharge.financial.dto.response.FaqResponse;
import com.freecharge.financial.dto.response.ProductDetailsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.freecharge.financial.constants.GoldCacheConstants.FAQ;
import static com.freecharge.financial.constants.GoldCacheConstants.GRAPH_CACHE;


@Slf4j
@Service
@CacheConfig(cacheManager = "goldRedisCacheManager")
public class GoldRedisCacheServiceImpl implements IGoldRedisCacheService {

    @Override
    @Cacheable(value = GRAPH_CACHE, key = "#url")
    public String getGraphDataCache(String url) {
        return null;
    }

    @Override
    @CachePut(value = GRAPH_CACHE, key = "#url")
    public String putGraphDataCache(String url, String response) {
        return response;
    }

    @Override
    @Cacheable(value = FAQ, key = "#url")
    public String getFaqCache(String url) {
        return null;
    }

    @Override
    @CachePut(value = FAQ, key = "#url")
    public String putFaqCache(String url,  String response) {
        return response;
    }

    @Override
    @Cacheable(value = GoldCacheConstants.UPM_TRIGGER_EVENT_COUNT, key = "#key")
    public String getUpmTriggerCountCache(String key) {
        return null;
    }

    @Override
    @CachePut(value = GoldCacheConstants.UPM_TRIGGER_EVENT_COUNT, key = "#key")
    public String putUpmTriggerCountCache(String key, String count) {
        log.info("UPM Trigger count cache generated");
        return count;
    }

    @Override
    @Cacheable(value = GoldCacheConstants.USER_REG_PAGE_COUNT, key = "#key")
    public String getUserRegisterCountPage(String key) {
        return null;
    }

    @Override
    @CachePut(value = GoldCacheConstants.USER_REG_PAGE_COUNT, key = "#key")
    public String putUserRegisterPageCountCache(String key, String count) {
        log.info("User count cache generated");
        return count;
    }

    @Override
    @Cacheable(value = GoldCacheConstants.GOLD_COIN_DETAIL, key = "#key")
    public ProductDetailsResponse getProductDetails(String key) {
        log.info("Product details cache fetched");
        return null;
    }
    @Override
    @CachePut(value = GoldCacheConstants.GOLD_COIN_DETAIL, key = "#key")
    public ProductDetailsResponse putProductDetails(String key, ProductDetailsResponse response) {
        log.info("Product details cache generated");
        return response;
    }

    @Override
    @CacheEvict(value = GoldCacheConstants.GOLD_COIN_DETAIL, key ="#key" )
    public void flushProductDetails(String key) {
        log.info("Product details removed from the cache");
    }


}
