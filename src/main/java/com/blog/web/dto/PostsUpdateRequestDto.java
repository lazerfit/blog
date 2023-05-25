package com.blog.web.dto;

import lombok.Builder;

@Builder
public record PostsUpdateRequestDto(String title, String content) {

}
