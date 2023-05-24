package com.blog.web.dto;

import static java.lang.Math.max;
import static java.lang.Math.min;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostsSearchRequestDto {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private final Integer page=1;

    @Builder.Default
    private final Integer size=10;

    public long getOffset() {
        return (long) (max(1,page)-1)*min(size,MAX_SIZE);
    }
}
