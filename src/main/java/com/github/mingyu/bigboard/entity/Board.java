package com.github.mingyu.bigboard.entity;

import com.github.mingyu.bigboard.dto.BoardCreateRequest;
import com.github.mingyu.bigboard.dto.BoardDetailResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column
    private String content;

    @Column(nullable = false)
    private String authorId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column
    private Integer viewCount=0;

    @Column(nullable = false)
    private Integer totalScore = 0;

    @Column(nullable = false)
    private Integer ratingCount = 0;

    public double getAverageScore() {
        return ratingCount == 0 ? 0 : (double) totalScore / ratingCount;
    }

    public static Board toBoard(BoardCreateRequest boardCreateRequest) {
        return Board.builder()
                .boardId(boardCreateRequest.getBoardId())
                .title(boardCreateRequest.getTitle())
                .content(boardCreateRequest.getContent())
                .authorId(boardCreateRequest.getAuthorId())
                .createdAt(boardCreateRequest.getCreatedAt())
                .build();
    }

}
