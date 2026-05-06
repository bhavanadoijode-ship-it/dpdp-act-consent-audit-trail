package com.internship.tool.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl
    .LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache
    .RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection
    .RedisConnectionFactory;
import org.springframework.data.redis.connection
    .RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce
    .LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer
    .GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer
    .RedisSerializationContext;
import org.springframework.data.redis.serializer
    .StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    // ── connection ───────────────────────────────────────────
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config =
            new RedisStandaloneConfiguration(
                redisHost, redisPort);
        return new LettuceConnectionFactory(config);
    }

    // ── serializer ───────────────────────────────────────────
    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY);
        return mapper;
    }

    @Bean
    public GenericJackson2JsonRedisSerializer redisSerializer() {
        return new GenericJackson2JsonRedisSerializer(
            redisObjectMapper());
    }

    // ── template ─────────────────────────────────────────────
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template =
            new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(
            new StringRedisSerializer());
        template.setValueSerializer(redisSerializer());
        template.setHashKeySerializer(
            new StringRedisSerializer());
        template.setHashValueSerializer(redisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    // ── cache manager ────────────────────────────────────────
    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory factory) {

        RedisCacheConfiguration defaultConfig =
            RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(
                    RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(
                            new StringRedisSerializer()))
                .serializeValuesWith(
                    RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(redisSerializer()))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigs =
            new HashMap<>();

        cacheConfigs.put(CacheNames.CONSENT_RECORDS,
            defaultConfig.entryTtl(Duration.ofMinutes(10)));

        cacheConfigs.put(CacheNames.CONSENT_RECORD_BY_ID,
            defaultConfig.entryTtl(Duration.ofMinutes(10)));

        cacheConfigs.put(CacheNames.CONSENT_STATS,
            defaultConfig.entryTtl(Duration.ofMinutes(5)));

        cacheConfigs.put(CacheNames.CONSENT_SEARCH,
            defaultConfig.entryTtl(Duration.ofMinutes(3)));

        return RedisCacheManager.builder(factory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigs)
            .build();
    }
}