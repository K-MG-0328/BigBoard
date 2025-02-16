package com.github.mingyu.bigboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardScoreRequest {
    private Long boardId;
    private double score= 0.0;
    private double totalScore = 0.0;
    private int ratingCount = 0;

    public double getAverageScore() {
        return ratingCount == 0 ? 0 : totalScore / ratingCount;
    }

    public BoardScoreServiceRequest toBoardScoreServiceRequest() {
        return BoardScoreServiceRequest.builder()
                .boardId(this.boardId)
                .score(this.score)
                .totalScore(this.totalScore)
                .ratingCount(this.ratingCount)
                .build();

    }
}
