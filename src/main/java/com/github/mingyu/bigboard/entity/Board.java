package com.github.mingyu.bigboard.entity;

import com.github.mingyu.bigboard.dto.BoardDetailRequest;
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

    @Column
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private int viewCount=0;

    @Column(nullable = false)
    private int totalScore = 0;

    @Column(nullable = false)
    private int ratingCount = 0;

    public double getAverageScore() {
        return ratingCount == 0 ? 0 : (double) totalScore / ratingCount;
    }

    public static Board toBoard(BoardDetailRequest boardDetailRequest) {
        return Board.builder()
                .boardId(boardDetailRequest.getBoardId())
                .title(boardDetailRequest.getTitle())
                .content(boardDetailRequest.getContent())
                .authorId(boardDetailRequest.getAuthorId())
                .createdAt(boardDetailRequest.getCreatedAt())
                .build();
    }

}
