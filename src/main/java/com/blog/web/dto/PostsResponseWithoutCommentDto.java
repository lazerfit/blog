package com.blog.web.dto;

import com.blog.domain.category.Category;
import com.blog.domain.posts.Posts;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PostsResponseWithoutCommentDto {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdDate;
    private final Category category;
    private final String tags;
    private final Long hit;


    public PostsResponseWithoutCommentDto(Posts posts) {
        this.id=posts.getId();
        this.title=posts.getTitle();
        this.content=posts.getContent();
        this.createdDate=posts.getCreateDate();
        this.category =posts.getCategory();
        this.tags=posts.getTags();
        this.hit=posts.getHit();
    }

    @QueryProjection
    public PostsResponseWithoutCommentDto(Long id, String title, String content, LocalDateTime createdDate,
        Category category, String tags, Long hit) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.category = category;
        this.tags = tags;
        this.hit = hit;
    }
}
