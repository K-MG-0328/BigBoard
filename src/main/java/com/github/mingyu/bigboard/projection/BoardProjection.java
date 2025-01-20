package com.github.mingyu.bigboard.projection;

import java.time.LocalDateTime;

public interface BoardProjection {
    Long getBoardId();
    String getTitle();
    String getAuthorId();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    Integer getViewCount();
}
