package com.github.mingyu.bigboard.service;

import com.github.mingyu.bigboard.dto.BoardDetailRequest;
import com.github.mingyu.bigboard.dto.BoardDetailResponse;
import com.github.mingyu.bigboard.dto.BoardResponse;
import com.github.mingyu.bigboard.dto.BoardScore;
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
    public BoardDetailResponse createBoard(BoardDetailRequest boardDetailRequest) {
        Board board = Board.toBoard(boardDetailRequest);
        boardRepository.save(board);
        return BoardDetailResponse.toBoardDetailResponse(board);
    }

    //게시글 목록 조회
    public Page<BoardResponse> getAllBoards(Pageable pageable) {
        return boardRepository.findBoardAll(pageable);
    }

    //게시글 조회
    @Transactional
    public BoardDetailResponse getBoardById(Long boardId){
        boardRepository.incrementViewCount(boardId);
        Board board = boardRepository.findById(boardId).orElse(null);
        return BoardDetailResponse.toBoardDetailResponse(board);
    }

    //게시글 수정
    public BoardDetailResponse updateBoard(BoardDetailRequest updateBoard){
        Board board = boardRepository.save(Board.toBoard(updateBoard));
        return BoardDetailResponse.toBoardDetailResponse(board);
    }

    //게시글 삭제
    public void deleteBoard(Long boardId){
        boardRepository.deleteById(boardId);
    }

    //평점 추가
    public double updateBoardRating(BoardScore board){
        boardRepository.evaluationBoard(board);
        BoardScore boardScore = boardRepository.getTotalScore(board.getBoardId());
        return boardScore.getAverageScore();
    }
}
