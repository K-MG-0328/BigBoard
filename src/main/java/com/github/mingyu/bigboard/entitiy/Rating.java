package com.github.mingyu.bigboard.entitiy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private Integer totalScore = 0;

    @Column(nullable = false)
    private Integer ratingCount = 0;

    public double getAverageScore() {
        return ratingCount == 0 ? 0 : (double) totalScore / ratingCount;
    }
}
