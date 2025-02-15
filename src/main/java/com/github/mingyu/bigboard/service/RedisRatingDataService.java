package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.dto.BoardScoreServiceRequest;
import com.github.mingyu.bigboard.projection.BoardScoreProjection;
import com.github.mingyu.bigboard.repository.RedisBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisRatingDataService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisBoardRepository boardRepository;

    public Set<String> getAllKeys(String pattern) {
        Set<String> keys = new HashSet<>();
        long cursor = 0;

        do{
            ScanOptions options = ScanOptions.scanOptions()
                    .match(pattern)
                    .count(100)
                    .build();

            Cursor<byte[]> redisCursor = redisTemplate
                    .executeWithStickyConnection(
                            redisConnection -> redisConnection.scan(options)
                    );

            Cursor<String> convertingCursor = new ConvertingCursor<>(redisCursor,
                    bytes -> new String(bytes, StandardCharsets.UTF_8)
            );

            while (convertingCursor.hasNext()) {
                keys.add(convertingCursor.next());
            }

            cursor = convertingCursor.getCursorId();


        }while(!(cursor == 0)); //cursor가 0이 되면 스캔 종료

        return keys != null ? keys : Set.of(); // keys가 null이면 빈 Set 반환
    }

    public void updateBoardRating(BoardScoreServiceRequest boardScore) {
        Long boardId = boardScore.getBoardId();
        double Score = boardScore.getScore();
        String key = "board:" + boardId + ":ratingData";

        Boolean chkKey = redisTemplate
                .opsForValue()
                .setIfAbsent(key, "0", Duration.ofMinutes(5));

        if (chkKey) { // 레디스에 키가 존재하는지 검증
            BoardScoreProjection ratingData = boardRepository.getRatingData(boardScore.getBoardId());

            int ratingCount = ratingData.getRatingCount()+1;
            double totalScore = ratingData.getTotalScore() + boardScore.getScore();

            redisTemplate.opsForHash().put(key, "totalScore", String.valueOf(totalScore));
            redisTemplate.opsForHash().put(key, "ratingCount", String.valueOf(ratingCount));
            redisTemplate.expire(key, Duration.ofMinutes(5));
        }

        redisTemplate.opsForHash().increment(key, "ratingCount", 1);
        redisTemplate.opsForHash().increment(key, "totalScore", Score);
        redisTemplate.expire(key, Duration.ofMinutes(5));
    }

    public double getTotalScore(String key) {
        Object totalScoreObj = redisTemplate.opsForHash().get(key, "totalScore");
        double totalScore = Double.parseDouble(String.valueOf(totalScoreObj));
        log.info("getTotalScore:totalScore {}", totalScore);
        return totalScore;
    }

    public int getRatingCount(String key) {
        Object totalScoreObj = redisTemplate.opsForHash().get(key, "ratingCount");
        int ratingCount = Integer.parseInt(String.valueOf(totalScoreObj));
        log.info("getRatingCount:ratingCount {}", ratingCount);
        return ratingCount;
    }

    @Transactional
    public void syncRatingData(Long boardId, double totalScore, int ratingCount) {
        boardRepository.updateRatingData(boardId, totalScore, ratingCount);
    }

}
