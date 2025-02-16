package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.repository.RedisBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisRatingSyncService {

    private final RedisBoardRepository boardRepository;

    @Transactional
    public void syncRatingData(Long boardId, double totalScore, int ratingCount) {
        boardRepository.updateRatingData(boardId, totalScore, ratingCount);
    }
}
