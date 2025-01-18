package com.github.mingyu.bigboard.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDetailDTO {
    private Long boardId;

    private String title;

    private String content;

    private String authorId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer viewCount;

    private Double rating;
}
