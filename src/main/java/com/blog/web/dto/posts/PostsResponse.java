package com.blog.web.dto.posts;

import com.blog.domain.posts.Post;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PostsResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdDate;
    private final String categoryTitle;
    private final String tag;
    private final Long views;
    private final String thumbnail;

    public PostsResponse(Post post) {
        this.id= post.getId();
        this.title= post.getTitle();
        this.content= post.getContent();
        this.createdDate= post.getGenerationTimeStamp();
        this.categoryTitle = post.getCategory().getTitle();
        this.tag = post.getTag();
        this.views = post.getViews();
        this.thumbnail=post.getThumbnail();
    }

    @QueryProjection
    public PostsResponse(Long id, String title, String content, LocalDateTime createdDate,
        String categoryTitle, String tag, Long views,String thumbnail) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.categoryTitle = categoryTitle;
        this.tag = tag;
        this.views = views;
        this.thumbnail=thumbnail;
    }
}
