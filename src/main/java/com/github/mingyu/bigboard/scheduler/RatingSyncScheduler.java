package com.github.mingyu.bigboard.scheduler;

import com.github.mingyu.bigboard.util.RedisKeyHelper;
import com.github.mingyu.bigboard.service.RedisRatingDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingSyncScheduler {

    private final RedisRatingDataService redisRatingDataService;

    @Scheduled(fixedRate = 60000) // 60초마다 동기화
    public void syncRatingData() {
        log.info("Starting syncRatingData...");
        Set<String> keys = redisRatingDataService.getAllKeys("board:*:ratingData");
        if (keys != null) {
            keys.forEach(key -> {
                Long boardId = RedisKeyHelper.extractBoardIdFromKey(key); //boardId 추출

                double totalScore = redisRatingDataService.getTotalScore(key);
                int ratingCount = redisRatingDataService.getRatingCount(key);

                redisRatingDataService.syncRatingData(boardId, totalScore, ratingCount);
            });
        }
    }
}
