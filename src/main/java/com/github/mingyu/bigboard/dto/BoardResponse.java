package com.github.mingyu.bigboard.dto;

import com.github.mingyu.bigboard.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardResponse {

    private Long boardId;
    private String title;
    private String authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer viewCount;

    public static BoardResponse toBoardResponse(Board board) {
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .authorId(board.getAuthorId())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .viewCount(board.getViewCount())
                .build();
    }

}
