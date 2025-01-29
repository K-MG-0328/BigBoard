package com.github.mingyu.bigboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardScore {
    private Long boardId;
    private double score= 0.0;
    private double totalScore = 0.0;
    private int ratingCount = 0;

    public double getAverageScore() {
        return ratingCount == 0 ? 0 : totalScore / ratingCount;
    }
}
