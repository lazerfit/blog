package com.blog.web.dto;

import com.blog.domain.posts.Posts;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostsResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime modifiedDate;

    public PostsResponseDto(Posts posts) {
        this.id=posts.getId();
        this.title=posts.getTitle();
        this.content=posts.getContent();
        this.modifiedDate=posts.getModifiedDate();
    }
}
