package com.github.mingyu.bigboard.dto;

import com.github.mingyu.bigboard.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDetailServiceRequest {
    private Long boardId;
    private String title;
    private String content;
    private String authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Board toBoard(){
        return Board.builder()
                .boardId(this.boardId)
                .title(this.title)
                .content(this.content)
                .authorId(this.authorId)
                .createdAt(this.createdAt != null ? this.createdAt : LocalDateTime.now())
                .updatedAt(this.updatedAt != null ? this.updatedAt : LocalDateTime.now())
                .build();
    }
}
