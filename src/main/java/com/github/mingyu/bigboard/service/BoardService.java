package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.dto.BoardCreateRequest;
import com.github.mingyu.bigboard.dto.BoardDetailResponse;
import com.github.mingyu.bigboard.entity.Board;
import com.github.mingyu.bigboard.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    //게시글 생성
    public Board createBoard(BoardCreateRequest boardCreateRequest) {
        return boardRepository.save(Board.toBoard(boardCreateRequest));
    }

    //게시글 목록 조회
    public Page<Board> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    //게시글 조회
    @Transactional
    public BoardDetailResponse getBoardById(Long boardId){
        boardRepository.incrementViewCount(boardId);
        Board board = boardRepository.findById(boardId).orElse(null);
        return BoardDetailResponse.toBoardDetailResponse(board);
    }

    //게시글 수정
    public Board updateBoard(Board updateBoard){
        return boardRepository.save(updateBoard);
    }

    //게시글 삭제
    public void deleteBoard(Long boardId){
        boardRepository.deleteById(boardId);
    }
}
