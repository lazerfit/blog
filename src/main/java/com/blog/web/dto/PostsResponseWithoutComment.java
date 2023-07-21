package com.blog.web.dto;

import com.blog.domain.category.Category;
import com.blog.domain.posts.Post;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PostsResponseWithoutComment {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdDate;
    private final Category category;
    private final String tags;
    private final Long hit;


    public PostsResponseWithoutComment(Post post) {
        this.id= post.getId();
        this.title= post.getTitle();
        this.content= post.getContent();
        this.createdDate= post.getGenerationTimeStamp();
        this.category = post.getCategory();
        this.tags= post.getTag();
        this.hit= post.getViews();
    }

    @QueryProjection
    public PostsResponseWithoutComment(Long id, String title, String content, LocalDateTime createdDate,
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
