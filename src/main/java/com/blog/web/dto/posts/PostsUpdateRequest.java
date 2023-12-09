package com.blog.web.dto.posts;

import com.blog.domain.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PostsUpdateRequest {

    private final String title;
    private final String content;
    private final String tag;
    private final Category category;
    private final String thumbnail;

    @Builder
    public PostsUpdateRequest(String title, String content, String tag, Category category,String thumbnail) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.category = category;
        this.thumbnail=thumbnail;
    }
}
