package com.github.mingyu.bigboard.repository;

import com.github.mingyu.bigboard.entitiy.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query("UPDATE Board SET viewCount = viewCount + 1 where boardId = :boardId")
    void incrementViewCount(@Param("boardId") Long boardId);
}
