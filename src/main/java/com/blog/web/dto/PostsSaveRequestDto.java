package com.blog.web.dto;

import com.blog.domain.category.Category;
import com.blog.domain.posts.Posts;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostsSaveRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;

    @NotBlank(message = "내용은 필수입니다.")
    private final String content;

    private final Category category;

    public Posts toEntity() {
        return Posts.builder()
            .title(title)
            .content(content)
            .category(category)
            .build();
    }
}
