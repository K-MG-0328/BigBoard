package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisViewCountService {
    private final RedisTemplate<String, String> redisTemplate;
    private final BoardRepository boardRepository;

    public Set<String> getAllKeys(String pattern) { //board:viewCount:*
        Set<String> keys = redisTemplate.keys(pattern); //특정 패턴에 매칭되는 키 검색
        if(keys == null) return Set.of();
        return keys.stream().map(Object::toString).collect(Collectors.toSet());
    }

    public int getViewCount(String key) {
        String value = redisTemplate.opsForValue().get(key);
        log.info("getViewCount:value {}", value);
        return value != null ? Integer.parseInt(value) : 0;
    }

    public void incrementViewCount(Long boardId) {
        String key = "board:" + boardId +":viewCount"; // 키 생성
        log.info("incrementViewCount:getViewCount {}", getViewCount(key));
        redisTemplate.opsForValue().increment(key);    // 키의 viewCount 값 증가
        redisTemplate.expire(key, Duration.ofDays(1L)); // TTL 설정 (1일)
    }

    @Transactional
    public void syncViewCount(Long boardId, int viewCount) {
        boardRepository.updateViewCount(boardId, viewCount);
    }
}
