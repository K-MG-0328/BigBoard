package com.github.mingyu.bigboard.scheduler;

import com.github.mingyu.bigboard.util.RedisKeyHelper;
import com.github.mingyu.bigboard.service.RedisViewCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewCountSyncScheduler {

    private final RedisViewCountService redisViewCountService;

    @Scheduled(fixedRate = 60000) // 60초마다 동기화
    public void syncViewCounts() {
        log.info("Starting view count sync...");
        Set<String> keys = redisViewCountService.getAllKeys("board:*:viewCount");
        if (keys != null && !keys.isEmpty()) {
            log.info("Keys.toString : {}", keys.toString());
            keys.forEach(key -> {
                Long boardId = RedisKeyHelper.extractBoardIdFromKey(key); //key에서 boardId 추출
                int viewCount = redisViewCountService.getViewCount(key);  //Redis에서 해당 키의 조회수 가져오기
                log.info("Sync view count for boardId: {}, viewCount: {}", boardId, viewCount);
                redisViewCountService.syncViewCount(boardId, viewCount);      //DB 동기화
            });
        }
    }
}
