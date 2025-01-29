package com.github.mingyu.bigboard.controller;

import com.github.mingyu.bigboard.dto.BoardDetailRequest;
import com.github.mingyu.bigboard.dto.BoardDetailResponse;
import com.github.mingyu.bigboard.dto.BoardResponse;
import com.github.mingyu.bigboard.dto.BoardScore;
import com.github.mingyu.bigboard.service.RedisBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class RedisBoardController {

    private final RedisBoardService boardService;

    //게시글 생성
    @PostMapping
    public ResponseEntity<BoardDetailResponse> createBoard(@RequestBody BoardDetailRequest board) {
        return ResponseEntity.ok(boardService.createBoard(board));
    }

    //게시글 목록 조회 Redis 적용 후
    @GetMapping()
    public List<BoardResponse> getBoards(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size
    ) {
        return boardService.getBoards(page, size);
    }

    //게시글 상세 조회 Redis 적용 후
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

    //평가 Redis 적용 후
    @PutMapping("/evaluation")
    public ResponseEntity<Void> updateBoardRating(@RequestBody BoardScore boardScore) {
        boardService.updateBoardRating(boardScore);
        return ResponseEntity.noContent().build();
    }
}
