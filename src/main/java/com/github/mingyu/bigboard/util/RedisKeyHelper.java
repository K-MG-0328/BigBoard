package com.github.mingyu.bigboard.util;

public class RedisKeyHelper {

    // Redis 키에서 boardId 추출
    public static Long extractBoardIdFromKey(String key) {
        String[] parts = key.split(":");  // ':' 기준으로 키를 나눔
        if (parts.length >= 3) {
            return Long.valueOf(parts[1]);      // 두 번째 부분이 boardId
        }
        throw new IllegalArgumentException("Invalid Redis key format: " + key);
    }

}
