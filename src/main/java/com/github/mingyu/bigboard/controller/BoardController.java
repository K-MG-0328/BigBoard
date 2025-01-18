package com.github.mingyu.bigboard.controller;

import com.github.mingyu.bigboard.entitiy.Board;
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

    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestBody Board board) {
        return ResponseEntity.ok(boardService.createBoard(board));
    }

    @GetMapping
    public ResponseEntity<Page<Board>> getBoards(Pageable pageable) {
        return ResponseEntity.ok(boardService.getAllBoards(pageable));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Board> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoardById(boardId));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Board> updateBoard(@PathVariable Long boardId, @RequestBody Board board) {
        return ResponseEntity.ok(boardService.updateBoard(board.getBoardId(), board));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deletBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }
}
