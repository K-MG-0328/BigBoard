package com.github.mingyu.bigboard.repository;


import com.github.mingyu.bigboard.entity.Board;
import com.github.mingyu.bigboard.projection.BoardProjection;
import com.github.mingyu.bigboard.projection.BoardScoreProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RedisBoardRepository extends JpaRepository<Board, Long> {

    //게시글 목록 조회 content 컬럼 제외
    @Query("select b.boardId as boardId, b.title as title, b.authorId as authorId, " +
            "       b.createdAt as createdAt, b.updatedAt as updatedAt, b.viewCount as viewCount " +
            "from Board b")
    Page<BoardProjection> findBoardAll(Pageable pageable);

    //조회수 조회
    @Query("select viewCount from Board where boardId = :boardId")
    int getViewCount(@Param("boardId") Long boardId);

    //조회수 일괄 업데이트
    @Modifying
    @Query("UPDATE Board b SET b.viewCount = :viewCount WHERE b.boardId = :boardId")
    void updateViewCount(@Param("boardId") Long boardId, @Param("viewCount") int viewCount);

    //평점 조회
    @Query("select b.totalScore as totalScore, b.ratingCount as ratingCount from Board b where b.boardId = :boardId")
    BoardScoreProjection getRatingData(@Param("boardId") Long boardId);

    //평점데이터 일괄 업데이트
    @Modifying
    @Query("UPDATE Board b SET b.totalScore = :totalScore, b.ratingCount = :ratingCount WHERE b.boardId = :boardId")
    void updateRatingData(Long boardId, double totalScore, int ratingCount);
}
