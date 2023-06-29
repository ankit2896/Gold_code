package com.freecharge.financial.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.freecharge.financial.constants.GoldCacheConstants.*;

@Configuration
@Slf4j
@EnableCaching
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@DependsOn("vaultConfig")
public class CacheConfiguration {

    @Value("${mf.redis.connection.host}")
    private String redisHost;
    @Value("${mf.redis.connection.port}")
    private int redisPort;
    @Value("${mf.redis.connection.timeout}")
    private int timeout;
    @Value("${mf.redis.connection.dataBase}")
    private int dataBase;
    @Value("${mf.redis.connection.max.active}")
    private int maxActive;

    @Value("${mf.redis.password}")
    private String password;

    @Value("${spring.profiles.active:qa}")
    private String env;

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    public CacheConfiguration() {
    }
    @Bean({"jedisPool"})
    public JedisPool jedisPool() {
        log.info("jedis pool details : hostname {} port Number {} database {}", new Object[]{this.redisHost, this.redisPort, this.dataBase});
        return new JedisPool(this.jedisPoolConfig(), this.redisHost, Integer.valueOf(this.redisPort), 8000, this.password, Integer.valueOf(this.dataBase), true);
    }


    @Bean
    public JedisPoolConfig jedisPoolConfig() {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(this.maxActive);
        poolConfig.setMaxIdle(30);
        poolConfig.setMinIdle(10);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(300).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds((long)300).toMillis());
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWaitMillis((long)64);
        return poolConfig;
    }


    @Primary
    @Bean(name = "goldJedisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(this.redisHost);
        factory.setPort(this.redisPort);
        factory.setTimeout(this.timeout);
        factory.setPassword(this.password);
        factory.setUseSsl(true);
        factory.setDatabase(this.dataBase);
        factory.setPoolConfig(this.jedisPoolConfig());
        factory.setUsePool(true);
        factory.getPoolConfig().setMaxIdle(30);
        factory.getPoolConfig().setMinIdle(10);
        log.info("jedisConnectionFactory details : hostname {} port Number {} database {}", new Object[]{this.redisHost, this.redisPort, this.dataBase});
        return factory;
    }

    private RedisTemplate<String, Object> redisTemplate;

    @Primary
    @Bean(name = "goldDaoRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("goldJedisConnectionFactory") JedisConnectionFactory cf) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(cf);
        template.setKeySerializer(stringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }



   @Primary
    @Bean(name = "goldRedisCacheManager")
    public CacheManager cacheManager(@Qualifier("goldDaoRedisTemplate") RedisTemplate template) {
        final String delimiter = ":";
        final String redis_client_name = "DG";
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .computePrefixWith(cacheName -> redis_client_name.concat(delimiter).concat(cacheName).concat(delimiter));

        cacheConfigurations.put(GRAPH_CACHE, defaultCacheConfiguration.entryTtl(Duration.ofHours(24)));// 24 hrs
        cacheConfigurations.put(UPM_TRIGGER_EVENT_COUNT,defaultCacheConfiguration.entryTtl(Duration.ofDays(365)));// 1 year
        cacheConfigurations.put(USER_REG_PAGE_COUNT,defaultCacheConfiguration.entryTtl(Duration.ofDays(365)));// 1 year
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(Objects.requireNonNull(template.getConnectionFactory()));
        return new RedisCacheManager(redisCacheWriter, defaultCacheConfiguration, cacheConfigurations);
    }
}