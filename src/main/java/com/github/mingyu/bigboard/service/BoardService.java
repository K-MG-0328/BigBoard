package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.dto.BoardDetailRequest;
import com.github.mingyu.bigboard.dto.BoardDetailResponse;
import com.github.mingyu.bigboard.dto.BoardResponse;
import com.github.mingyu.bigboard.dto.BoardScore;
import com.github.mingyu.bigboard.entity.Board;
import com.github.mingyu.bigboard.projection.BoardProjection;
import com.github.mingyu.bigboard.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final RedisViewCountService redisViewCountService;

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

    //게시글 목록 조회 Redis 적용 후
    @Cacheable(cacheNames = "getBoards", key = "'boards:page:' + #page + ':size:' + #size", cacheManager = "cacheManager")
    public List<BoardResponse> getBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardProjection> pageOfBoards = boardRepository.findBoardAll(pageable);

        List<BoardResponse> boardResponseList = pageOfBoards
            .stream()
            .map(projection -> new BoardResponse().toBoardResponse(projection))
            .toList();

        return boardResponseList;
    }

    //게시글 조회 Redis 적용 전
    @Transactional
    public BoardDetailResponse getBoardByIdBefore(Long boardId){
        boardRepository.incrementViewCount(boardId);
        Board board = boardRepository.findById(boardId).orElse(null);
        return BoardDetailResponse.toBoardDetailResponse(board);
    }

    //게시글 조회 Redis 적용 후
    public BoardDetailResponse getBoardById(Long boardId) {
        redisViewCountService.incrementViewCount(boardId); //조회수를 캐싱
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
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

    //평점 추가
    public double updateBoardRating(BoardScore boardScore){
        Board board = boardRepository.findById(boardScore.getBoardId()).orElse(null);
        board.setRatingCount(board.getRatingCount()+1);
        board.setTotalScore(board.getTotalScore() + boardScore.getScore());
        boardRepository.save(board);
        return board.getAverageScore();
    }
}
