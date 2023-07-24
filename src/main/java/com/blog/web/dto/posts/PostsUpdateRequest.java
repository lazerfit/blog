package com.blog.web.dto.posts;

import com.blog.domain.category.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsUpdateRequest {

    private final String title;
    private final String content;
    private final String tag;
    private final Category category;

    @Builder
    public PostsUpdateRequest(String title, String content, String tag, Category category) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.category = category;
    }
}
