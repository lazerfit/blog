package com.blog.web.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostsResponseWithCategoryDto {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdDate;
    private final String categoryTitle;

    public PostsResponseWithCategoryDto(Long id, String title, String content,
        LocalDateTime createdDate,
        String categoryTitle) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.categoryTitle = categoryTitle;
    }
}
