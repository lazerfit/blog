package com.blog.web.dto;

import com.blog.domain.category.Category;
import lombok.Builder;

@Builder
public record PostsUpdateRequestDto(String title, String content, Category category) {

}
