package com.blog.web.dto.posts;

import com.blog.domain.category.Category;
import com.blog.domain.posts.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PostSaveRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;
    @NotBlank(message = "내용은 필수입니다.")
    private final String content;
    @NotBlank(message = "카테고리 선택은 필수입니다.")
    private final Category category;
    private final String tags;
    private final Long views;
    private final TagHandler tagHandler=new TagHandler();

    public Post toEntity(){
        return Post.builder()
            .title(title)
            .content(content)
            .category(category)
            .tag(tagHandler.setDefaultValueOrTransformJsonArrayToString(tags))
            .views(views)
            .build();
    }
}
