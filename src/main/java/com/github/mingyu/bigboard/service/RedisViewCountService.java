package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.repository.RedisBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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

    public void incrementViewCount(Long boardId) {
        String key = "board:" + boardId + ":viewCount"; // 키 생성

        /* TTL로 레디스에서 캐시가 사라지게 되면 조회수가 초기화될 가능성이 있으므로 검증로직을 추가*/
        synchronized (this) { // 동시성 문제를 방지하기 위해 동기화 블록 추가
            if (!redisTemplate.hasKey(key)) { // 레디스에 키가 존재하는지 검증
                int viewCount = boardRepository.getViewCount(boardId);
                redisTemplate.opsForValue().set(key, String.valueOf(viewCount), Duration.ofDays(1L)); // TTL 설정
            }
            redisTemplate.opsForValue().increment(key); // 키의 viewCount 값 증가
        }
    }

    @Transactional
    public void syncViewCount(Long boardId, int viewCount) {
        boardRepository.updateViewCount(boardId, viewCount);
    }
}
