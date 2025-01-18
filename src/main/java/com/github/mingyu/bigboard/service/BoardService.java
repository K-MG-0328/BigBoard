package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.dto.BoardDetailDTO;
import com.github.mingyu.bigboard.entitiy.Board;
import com.github.mingyu.bigboard.entitiy.Rating;
import com.github.mingyu.bigboard.repository.BoardRepository;
import com.github.mingyu.bigboard.repository.RatingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final RatingRepository ratingRepository;

    //게시글 생성
    public Board createBoard(Board board){
        return boardRepository.save(board);
    }

    //게시글 목록 조회
    public Page<Board> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }
    
    //게시글 조회
    public Board getBoardById(Long boardId){
        boardRepository.incrementViewCount(boardId);
        Board board = boardRepository.findById(boardId).orElse(null);
        Rating rating = ratingRepository.findById(boardId).orElse(null);



        return boardRepository.findById(boardId).orElse(null);
    }

    //게시글 수정
    public Board updateBoard(Long boardId, Board updateBoard){

        //객체를 받아올 때 객체가 null 일 경우 문제가 생길 수 있음.
        Board board = boardRepository.findById(boardId).orElse(null);
        board.setTitle(updateBoard.getTitle());
        board.setContent(updateBoard.getTitle());
        board.setUpdatedAt(LocalDateTime.now());
        return boardRepository.save(board);
    }

    //게시글 삭제
    public void deleteBoard(Long boardId){
        boardRepository.deleteById(boardId);
    }
}
