package com.blog.web.dto.posts;

import com.blog.domain.category.Category;
import lombok.Builder;

@Builder
public record PostsUpdateRequest(String title, String content, Category category) {

}
