package com.blog.web.dto;

import java.time.LocalDateTime;


public record PostsResponseWithCategoryDto(Long id, String title, String content,
                                           LocalDateTime createdDate, String categoryTitle) {

}
