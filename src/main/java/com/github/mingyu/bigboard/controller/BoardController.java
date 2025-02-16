package com.github.mingyu.bigboard.controller;

import com.github.mingyu.bigboard.dto.*;
import com.github.mingyu.bigboard.projection.BoardProjection;
import com.github.mingyu.bigboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    //게시글 생성
    @PostMapping("/before/board")
    public ResponseEntity<BoardDetailResponse> createBoard(@RequestBody BoardDetailRequest request) {
        BoardDetailServiceRequest boardDetail = request.toBoardDetailServiceRequest();
        return ResponseEntity.ok(boardService.createBoard(boardDetail));
    }

    //게시글 목록 조회 Redis 적용 전
    @GetMapping("/before/board")
    public ResponseEntity<Page<BoardProjection>> getBoards(Pageable pageable) {
        return ResponseEntity.ok(boardService.getAllBoardsBefore(pageable));
    }

    //게시글 상세 조회 Redis 적용 전
    @GetMapping("/before/board/{boardId}")
    public ResponseEntity<BoardDetailResponse> getBoardBefore(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoardByIdBefore(boardId));
    }

    //게시글 수정
    @PutMapping("/before/board/{boardId}")
    public ResponseEntity<BoardDetailResponse> updateBoard(@RequestBody BoardDetailRequest request, @RequestParam String userId) throws AccessDeniedException {
        BoardDetailServiceRequest boardDetail = request.toBoardDetailServiceRequest();
        return ResponseEntity.ok(boardService.updateBoard(boardDetail, userId));
    }

    //게시글 삭제
    @DeleteMapping("/before/board/{boardId}")
    public ResponseEntity<Void> deletBoard(@RequestBody BoardDetailRequest request, @RequestParam String userId) throws AccessDeniedException {
        BoardDetailServiceRequest boardDetail = request.toBoardDetailServiceRequest();
        boardService.deleteBoard(boardDetail, userId);
        return ResponseEntity.noContent().build();
    }

    //평가 Redis 적용 전
    @RequestMapping("/before/board/evaluation")
    public ResponseEntity<Double> updateBoardRating(@RequestBody BoardScoreRequest request) {
        BoardScoreServiceRequest boardScore = request.toBoardScoreServiceRequest();
        return ResponseEntity.ok(boardService.updateBoardRating(boardScore));
    }
}
