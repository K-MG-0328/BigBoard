package com.github.mingyu.bigboard.controller;

import com.github.mingyu.bigboard.dto.BoardDetailRequest;
import com.github.mingyu.bigboard.dto.BoardDetailResponse;
import com.github.mingyu.bigboard.dto.BoardScore;
import com.github.mingyu.bigboard.projection.BoardProjection;
import com.github.mingyu.bigboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시글 생성
    @PostMapping
    public ResponseEntity<BoardDetailResponse> createBoard(@RequestBody BoardDetailRequest board) {
        return ResponseEntity.ok(boardService.createBoard(board));
    }

    //게시글 목록 조회
    @GetMapping
    public ResponseEntity<Page<BoardProjection>> getBoards(Pageable pageable) {
        return ResponseEntity.ok(boardService.getAllBoards(pageable));
    }

    //게시글 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailResponse> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoardById(boardId));
    }

    //게시글 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardDetailResponse> updateBoard(@RequestBody BoardDetailRequest board) {
        return ResponseEntity.ok(boardService.updateBoard(board));
    }

    //게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deletBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    //평가
    @PutMapping("/evaluation")
    public ResponseEntity<Double> updateBoardRating(@RequestBody BoardScore boardScore) {
        return ResponseEntity.ok(boardService.updateBoardRating(boardScore));
    }
}
