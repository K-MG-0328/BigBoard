package com.github.mingyu.bigboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.mingyu.bigboard.projection.BoardProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@ToString
public class BoardResponse {

    private Long boardId;
    private String title;
    private String authorId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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

    public static BoardResponse toBoardResponse(BoardProjection boardProjection) {
        return BoardResponse.builder()
                .boardId(boardProjection.getBoardId())
                .title(boardProjection.getTitle())
                .authorId(boardProjection.getAuthorId())
                .createdAt(boardProjection.getCreatedAt())
                .updatedAt(boardProjection.getUpdatedAt())
                .viewCount(boardProjection.getViewCount())
                .build();
    }

}
