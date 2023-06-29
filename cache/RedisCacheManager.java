package com.freecharge.financial.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class RedisCacheManager implements CacheManager {


    @Autowired
    private RedisTemplate<String, Object> redisCacheTemplate;


    @Override
    public <T> T get(String key) {
        return (T) redisCacheTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, Object value) {
        redisCacheTemplate.opsForValue().set(key, value);
    }

    //@Override
    public Boolean setIfAbsent(String key, Object value) {
        return redisCacheTemplate.opsForValue().setIfAbsent(key, value);
    }



    @Override
    public void set(String key, Object value, final long timeOut, TimeUnit timeUnit) {
        redisCacheTemplate.opsForValue().set(key, value, timeOut, timeUnit);
    }

    @Override
    public void delete(String key) {
        redisCacheTemplate.delete(key);
    }


}
