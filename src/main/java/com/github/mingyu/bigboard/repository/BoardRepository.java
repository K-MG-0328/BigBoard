package com.github.mingyu.bigboard.repository;

import com.github.mingyu.bigboard.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    //조회수 증가
    @Modifying
    @Query("UPDATE Board SET viewCount = viewCount + 1 where boardId = :boardId")
    void incrementViewCount(@Param("boardId") Long boardId);
}
