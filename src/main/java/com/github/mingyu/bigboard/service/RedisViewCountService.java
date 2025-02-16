package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.repository.RedisBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisViewCountService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisBoardRepository boardRepository;

    public Set<String> getAllKeys(String pattern) { //board:viewCount:*
        Set<String> keys = redisTemplate.keys(pattern); //특정 패턴에 매칭되는 키 검색
        return keys != null ? keys : Set.of(); // keys가 null이면 빈 Set 반환
    }

    public int getViewCount(String key) {
        String value = redisTemplate.opsForValue().get(key);
        log.info("getViewCount:value {}", value);
        return value != null ? Integer.parseInt(value) : 0;
    }

    @Async
    public void incrementViewCount(Long boardId) {
        String key = "board:" + boardId + ":viewCount"; // 키 생성

        // 키가 없으면 (원자적으로) "0" 을 세팅하고 true 반환,
        // 키가 있으면 false 반환
        Boolean chkKey = redisTemplate
                .opsForValue()
                .setIfAbsent(key, "0", Duration.ofMinutes(5));

        if (chkKey) {
            int viewCount = boardRepository.getViewCount(boardId);
            redisTemplate.opsForValue().set(key, String.valueOf(viewCount), Duration.ofMinutes(5));
        }

        redisTemplate.opsForValue().increment(key); // 키의 viewCount 값 증가
        redisTemplate.expire(key, Duration.ofMinutes(5)); //ttl과 db 싱크하는 주기가 엇갈린다면 데이터 유실이 발생할 수 있음 expire 추가함으로써 방지

    }

    @Transactional
    public void syncViewCount(Long boardId, int viewCount) {
        boardRepository.updateViewCount(boardId, viewCount);
    }
}
