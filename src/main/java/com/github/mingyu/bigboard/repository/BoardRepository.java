package com.github.mingyu.bigboard.repository;

import com.github.mingyu.bigboard.dto.BoardResponse;
import com.github.mingyu.bigboard.dto.BoardScore;
import com.github.mingyu.bigboard.entity.Board;
import com.github.mingyu.bigboard.projection.BoardProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    //게시글 목록 조회 content 컬럼 제외
    @Query("select b.boardId as boardId, b.title as title, b.authorId as authorId, " +
            "       b.createdAt as createdAt, b.updatedAt as updatedAt, b.viewCount as viewCount " +
            "from Board b")
    Page<BoardProjection> findBoardAll(Pageable pageable);

    //조회수 증가
    @Modifying
    @Query("update Board set viewCount = viewCount + 1 where boardId = :boardId")
    void incrementViewCount(@Param("boardId") Long boardId);

    //평점 추가
    @Modifying
    @Query("update Board set totalScore = totalScore + :#{#boardScore.score}, ratingCount = ratingCount + 1 where boardId = :#{#boardScore.boardId}")
    void evaluationBoard(@Param("board") BoardScore boardScore);

    //평점 조회
    @Query("select totalScore, ratingCount from Board where boardId = :boardId")
    BoardScore getTotalScore(@Param("boardId") Long boardId);
}
