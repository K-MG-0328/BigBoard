package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.dto.BoardDetailResponse;
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

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisRatingDataService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisBoardRepository boardRepository;
    private final RedisRatingSyncService redisRatingSyncService;

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


    public BoardDetailResponse updateBoardRating(BoardScoreServiceRequest boardScore) {
        Long boardId = boardScore.getBoardId();
        double score = boardScore.getScore();
        String key = "board:" + boardId + ":ratingData";

        Map<Object, Object> ratingMap = redisTemplate.opsForHash().entries(key);

        if (!ratingMap.isEmpty()) {
            log.info("캐시 히트. boardId={}, key={}", boardId, key);

            double currentTotalScore = Double.parseDouble((String) ratingMap.get("totalScore"));
            int currentRatingCount = Integer.parseInt((String) ratingMap.get("ratingCount"));

            double newTotalScore = currentTotalScore + score;
            int newRatingCount = currentRatingCount + 1;

            redisTemplate.opsForHash().put(key, "totalScore", String.valueOf(newTotalScore));
            redisTemplate.opsForHash().put(key, "ratingCount", String.valueOf(newRatingCount));
            redisTemplate.expire(key, Duration.ofMinutes(5)); // TTL 갱신

            redisRatingSyncService.syncRatingData(boardId, newTotalScore, newRatingCount);

            boardScore.setTotalScore(newTotalScore);
            boardScore.setRatingCount(newRatingCount);
            BoardDetailResponse boardDetailResponse = boardScore.toBoardDetailResponse();
            return boardDetailResponse;

        } else {
            log.info("캐시 미스. boardId={}, key={}", boardId, key);

            // DB 조회
            BoardScoreProjection ratingData = boardRepository.getRatingData(boardId);
            double currentTotalScore = ratingData.getTotalScore();
            int currentRatingCount   = ratingData.getRatingCount();

            // 새 점수를 반영
            double newTotalScore = currentTotalScore + score;
            int newRatingCount = currentRatingCount + 1;

            // 캐시에 저장
            redisTemplate.opsForHash().put(key, "totalScore", String.valueOf(newTotalScore));
            redisTemplate.opsForHash().put(key, "ratingCount", String.valueOf(newRatingCount));
            redisTemplate.expire(key, Duration.ofMinutes(5));

            redisRatingSyncService.syncRatingData(boardId, newTotalScore, newRatingCount);

            boardScore.setTotalScore(newTotalScore);
            boardScore.setRatingCount(newRatingCount);
            BoardDetailResponse boardDetailResponse = boardScore.toBoardDetailResponse();
            return boardDetailResponse;
        }
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

}
