package com.github.mingyu.bigboard.projection;

public interface BoardScoreProjection {
    Long getBoardId();
    Double getTotalScore();
    Integer getRatingCount();
}
