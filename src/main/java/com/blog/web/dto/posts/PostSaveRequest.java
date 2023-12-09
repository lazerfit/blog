package com.blog.web.dto.posts;

import com.blog.domain.category.Category;
import com.blog.domain.posts.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostSaveRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;
    @NotBlank(message = "내용은 필수입니다.")
    private final String content;
    @NotBlank(message = "카테고리 선택은 필수입니다.")
    private final Category category;
    private final String tags;
    private final Long views;
    @NotNull(message = "썸네일은 필수입니다.")
    private final String thumbnail;

    @Builder
    public PostSaveRequest(String title, String content, Category category, String tags, Long views,
        @NotNull(message = "썸네일은 필수입니다.") String thumbnail) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.views = views;
        this.thumbnail = thumbnail;
    }

    public Post toEntity(){
        return Post.builder()
            .title(title)
            .content(content)
            .category(category)
            .tag(tags)
            .views(views)
            .thumbnail(thumbnail)
            .build();
    }
}
