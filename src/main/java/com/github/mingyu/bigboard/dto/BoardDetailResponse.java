package com.github.mingyu.bigboard.dto;

import com.github.mingyu.bigboard.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDetailResponse {

    private Long boardId;
    private String title;
    private String content;
    private String authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer viewCount;

    private Double rating;

    public static BoardDetailResponse toBoardDetailResponse(Board board) {
        return BoardDetailResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .authorId(board.getAuthorId())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .viewCount(board.getViewCount())
                .rating(board.getAverageScore())
                .build();
    }

}
