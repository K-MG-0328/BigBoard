package com.github.mingyu.bigboard.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching //스프링 부트의 캐싱 설정을 활성화
public class RedisCacheConfig {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 지원
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );


        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig() //Redis 캐시의 기본 설정을 로드
                .serializeKeysWith(  //키를 직렬화하는 방식 설정
                    RedisSerializationContext
                            .SerializationPair // 직렬화, 역직렬화
                            .fromSerializer(new StringRedisSerializer()) // StringRedisSerializer : 문자열 형식으로 저장
                )
                .serializeValuesWith( //값을 직렬화하는 방식 설정
                    RedisSerializationContext
                            .SerializationPair
                            .fromSerializer(serializer)
                )
                //데이터 만료기간(TTL) 설정(5분 동안 유효)
                .entryTtl(Duration.ofMinutes(5L));

        return RedisCacheManager
                .RedisCacheManagerBuilder //캐시를 관리할 CacheManager 객체 생성
                .fromConnectionFactory(redisConnectionFactory) // 레디스 서버와 연결 처리
                .cacheDefaults(redisCacheConfiguration)  // 직렬화, TTL 등 설정
                .build();
    }
}
