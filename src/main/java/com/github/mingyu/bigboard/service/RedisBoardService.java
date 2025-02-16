package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.dto.*;
import com.github.mingyu.bigboard.entity.Board;
import com.github.mingyu.bigboard.projection.BoardProjection;
import com.github.mingyu.bigboard.repository.RedisBoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RedisBoardService {

    private final RedisBoardRepository boardRepository;
    private final RedisViewCountService redisViewCountService;
    private final RedisRatingDataService redisRatingDataService;

    //게시글 생성
    public BoardDetailResponse createBoard(BoardDetailServiceRequest boardDetail) {
        Board board = boardDetail.toBoard();
        boardRepository.save(board);
        return BoardDetailResponse.toBoardDetailResponse(board);
    }

    //게시글 목록 조회 Redis 적용 후
    @Cacheable(cacheNames = "getBoards", key = "'boards:page:' + #page + ':size:' + #size", cacheManager = "cacheManager")
    public BoardListResponse getBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardProjection> pageOfBoards = boardRepository.findBoardAll(pageable);

        List<BoardResponse> boardResponseList = pageOfBoards
                .stream()
                .map(projection -> new BoardResponse().toBoardResponse(projection))
                .toList();
        BoardListResponse boardListResponse = new BoardListResponse();
        boardListResponse.setBoards(boardResponseList);
        boardListResponse.setPageCnt(pageOfBoards.getNumberOfElements());
        boardListResponse.setBoardCnt(pageOfBoards.getTotalElements());
        boardListResponse.setCurrentPage(page);

        return boardListResponse;
    }

    //게시글 조회 Redis 적용 후
    public BoardDetailResponse getBoardById(Long boardId) {
        log.info("RedisBoardService::getBoardById");

        String key = "board:" + boardId + ":viewCount";

        redisViewCountService.incrementViewCount(boardId); //조회수를 캐싱
        int viewCount = redisViewCountService.getViewCount(key);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        board.setViewCount(viewCount);
        return BoardDetailResponse.toBoardDetailResponse(board);
    }

    //게시글 수정
    public BoardDetailResponse updateBoard(BoardDetailServiceRequest updateBoard, String userId) throws AccessDeniedException {

        //검증 로직
        if (!updateBoard.getAuthorId().equals(userId)) {
            throw new AccessDeniedException("본인이 작성한 글이 아닙니다.");
        }

        Board board = boardRepository.findById(updateBoard.getBoardId()).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        board.setContent(updateBoard.getContent());
        board.setUpdatedAt(updateBoard.getUpdatedAt());
        boardRepository.save(board);
        return BoardDetailResponse.toBoardDetailResponse(board);
    }

    //게시글 삭제
    public void deleteBoard(BoardDetailServiceRequest deleteBoard, String userId) throws AccessDeniedException {

        //검증 로직
        if (!deleteBoard.getAuthorId().equals(userId)) {
            throw new AccessDeniedException("본인이 작성한 글이 아닙니다.");
        }

        boardRepository.deleteById(deleteBoard.getBoardId());
    }

    //평점 추가 Redis 적용 후
    public BoardDetailResponse updateBoardRating(BoardScoreServiceRequest boardScore){
        //평점을 추가 했을 경우 레디스에 저장.
        return redisRatingDataService.updateBoardRating(boardScore);
    }
}
