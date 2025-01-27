package com.github.mingyu.bigboard.entity;

import com.github.mingyu.bigboard.dto.BoardDetailRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private int viewCount = 0;

    @Column
    private double totalScore = 0;

    @Column
    private int ratingCount = 0;

    public double getAverageScore() {
        return ratingCount == 0 ? 0 : (double) totalScore / ratingCount;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public static Board toBoard(BoardDetailRequest boardDetailRequest) {
        return Board.builder()
                .boardId(boardDetailRequest.getBoardId())
                .title(boardDetailRequest.getTitle())
                .content(boardDetailRequest.getContent())
                .authorId(boardDetailRequest.getAuthorId())
                .createdAt(boardDetailRequest.getCreatedAt() != null ? boardDetailRequest.getCreatedAt() : LocalDateTime.now())
                .updatedAt(boardDetailRequest.getUpdatedAt() != null ? boardDetailRequest.getUpdatedAt() : LocalDateTime.now())
                .build();
    }

}
