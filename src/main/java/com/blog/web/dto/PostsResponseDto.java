package com.blog.web.dto;

import com.blog.domain.posts.Posts;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PostsResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime modifiedDate;
    private final LocalDateTime createdDate;

    public PostsResponseDto(Posts posts) {
        this.id=posts.getId();
        this.title=posts.getTitle();
        this.content=posts.getContent();
        this.modifiedDate=posts.getModifiedDate();
        this.createdDate=posts.getCreateDate();
    }

    @QueryProjection
    public PostsResponseDto(Long id, String title, String content, LocalDateTime modifiedDate, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.modifiedDate = modifiedDate;
        this.createdDate=createdDate;
    }
}
