package com.github.mingyu.bigboard.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDetailRequest {

    private Long boardId;
    private String title;
    private String content;
    private String authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BoardDetailServiceRequest toBoardDetailServiceRequest(){
        return BoardDetailServiceRequest.builder()
                .boardId(this.boardId)
                .title(this.title)
                .content(this.content)
                .authorId(this.authorId)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

}
