package com.blog.web.dto.posts;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsIndexContent {

    private final Long id;
    private final String thumbnail;
    private final String title;
    private final String content;
    private final LocalDateTime createdDate;

    @Builder
    public PostsIndexContent(Long id, String thumbnail, String title, String content,
        LocalDateTime createdDate) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
    }
}
