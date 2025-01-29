package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.dto.BoardDetailRequest;
import com.github.mingyu.bigboard.dto.BoardDetailResponse;
import com.github.mingyu.bigboard.dto.BoardScore;
import com.github.mingyu.bigboard.entity.Board;
import com.github.mingyu.bigboard.projection.BoardProjection;
import com.github.mingyu.bigboard.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Deprecated
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final RedisViewCountService redisViewCountService;
    private final RedisRatingDataService redisRatingDataService;

    //게시글 생성
    public BoardDetailResponse createBoard(BoardDetailRequest boardDetailRequest) {
        Board board = Board.toBoard(boardDetailRequest);
        boardRepository.save(board);
        return BoardDetailResponse.toBoardDetailResponse(board);
    }

    //게시글 목록 조회 Redis 적용 전
    @Cacheable(
            cacheNames = "getBoards",
            key = "'boards:page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize",
            cacheManager = "cacheManager"
    )
    public Page<BoardProjection> getAllBoardsBefore(Pageable pageable) {
        return boardRepository.findBoardAll(pageable);
    }

    //게시글 조회 Redis 적용 전
    @Transactional
    public BoardDetailResponse getBoardByIdBefore(Long boardId){
        boardRepository.incrementViewCount(boardId);
        Board board = boardRepository.findById(boardId).orElse(null);
        return BoardDetailResponse.toBoardDetailResponse(board);
    }

    //게시글 수정
    public BoardDetailResponse updateBoard(BoardDetailRequest updateBoard){
        Board board = boardRepository.findById(updateBoard.getBoardId()).orElse(null);
        board.setContent(updateBoard.getContent());
        board.setUpdatedAt(updateBoard.getUpdatedAt());
        boardRepository.save(board);
        return BoardDetailResponse.toBoardDetailResponse(board);
    }

    //게시글 삭제
    public void deleteBoard(Long boardId){
        boardRepository.deleteById(boardId);
    }

    //평점 추가 Redis 적용 전
    public double updateBoardRating(BoardScore boardScore){
        Board board = boardRepository.findById(boardScore.getBoardId()).orElse(null);
        board.setRatingCount(board.getRatingCount()+1);
        board.setTotalScore(board.getTotalScore() + boardScore.getScore());
        boardRepository.save(board);
        return board.getAverageScore();
    }
}
