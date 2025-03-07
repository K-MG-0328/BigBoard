package com.github.mingyu.bigboard.controller;

import com.github.mingyu.bigboard.dto.*;
import com.github.mingyu.bigboard.service.RedisBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;


@RestController
@RequiredArgsConstructor
public class RedisBoardController {

    private final RedisBoardService boardService;

    //게시글 생성
    @PostMapping("/board")
    public ResponseEntity<BoardDetailResponse> createBoard(@RequestBody BoardDetailRequest request) {
        BoardDetailServiceRequest boardDetail = request.toBoardDetailServiceRequest();
        return ResponseEntity.ok(boardService.createBoard(boardDetail));
    }

    //게시글 목록 조회 Redis 적용 후
    @GetMapping("/board")
    public BoardListResponse getBoards(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size
    ) {
        return boardService.getBoards(page, size);
    }

    //게시글 상세 조회 Redis 적용 후
    @GetMapping("/board/{boardId}")
    public ResponseEntity<BoardDetailResponse> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoardById(boardId));
    }

    //게시글 수정
    @PutMapping("/board/{boardId}")
    public ResponseEntity<BoardDetailResponse> updateBoard(@RequestBody BoardDetailRequest request, @RequestParam String userId) throws AccessDeniedException {
        BoardDetailServiceRequest boardDetail = request.toBoardDetailServiceRequest();
        BoardDetailResponse boardDetailResponse =  boardService.updateBoard(boardDetail, userId);
        return ResponseEntity.ok(boardDetailResponse);
    }

    //게시글 삭제
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<Void> deleteBoard(@RequestBody BoardDetailRequest request, @RequestParam String userId) throws AccessDeniedException {
        BoardDetailServiceRequest boardDetail = request.toBoardDetailServiceRequest();
        boardService.deleteBoard(boardDetail , userId);
        return ResponseEntity.noContent().build();
    }

    //평가 Redis 적용 후
    @RequestMapping("/board/evaluation")
    public ResponseEntity<BoardDetailResponse> updateBoardRating(@RequestBody BoardScoreRequest request) {
        BoardScoreServiceRequest boardScore = request.toBoardScoreServiceRequest();
        BoardDetailResponse boardDetailResponse =  boardService.updateBoardRating(boardScore);
        return ResponseEntity.ok(boardDetailResponse);
    }
}
