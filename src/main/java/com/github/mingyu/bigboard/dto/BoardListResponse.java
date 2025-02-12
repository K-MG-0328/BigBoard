package com.github.mingyu.bigboard.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardListResponse {

    private int pageCnt;
    private long boardCnt;
    private int currentPage;
    private List<BoardResponse> boards;
}
