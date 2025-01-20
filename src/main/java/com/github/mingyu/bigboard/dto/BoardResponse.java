package com.github.mingyu.bigboard.dto;

import com.github.mingyu.bigboard.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Deprecated
@Getter
@Builder
@NoArgsConstructor
@ToString
public class BoardResponse {

    private Long boardId;
    private String title;
    private String authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer viewCount;

    public BoardResponse(Long boardId, String title, String authorId,
                         LocalDateTime createdAt, LocalDateTime updatedAt, Integer viewCount) {
        this.boardId = boardId;
        this.title = title;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
    }

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
