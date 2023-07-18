package com.blog.web.dto;

import com.blog.domain.category.Category;
import com.blog.domain.posts.Post;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PostsResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdDate;
    private final Category category;
    private final String tag;
    private final Long views;
    private List<CommentsResponseDto> comments;


    public PostsResponseDto(Post post) {
        this.id= post.getId();
        this.title= post.getTitle();
        this.content= post.getContent();
        this.createdDate= post.getGenerationTimeStamp();
        this.category = post.getCategory();
        this.tag = post.getTag();
        this.views = post.getViews();
        this.comments= post.getComments().stream().map(CommentsResponseDto::new).toList();
    }

    @QueryProjection
    public PostsResponseDto(Long id, String title, String content, LocalDateTime createdDate,
        Category category, String tag, Long views) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.category = category;
        this.tag = tag;
        this.views = views;
    }

    public void insertComment(List<CommentsResponseDto> responseDto) {
        this.comments=responseDto;
    }
}
