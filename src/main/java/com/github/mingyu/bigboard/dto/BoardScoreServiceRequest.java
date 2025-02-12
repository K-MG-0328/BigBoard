package com.github.mingyu.bigboard.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardScoreServiceRequest {
    private Long boardId;
    private double score= 0.0;
    private double totalScore = 0.0;
    private int ratingCount = 0;

    public double getAverageScore() {
        return ratingCount == 0 ? 0 : totalScore / ratingCount;
    }
}
