package com.blog.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryEditRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;
    @NotBlank(message = "정렬 순서는 필수입니다.")
    private final Integer listOrder;

    @Builder
    public CategoryEditRequestDto(String title, Integer listOrder) {
        this.title = title;
        this.listOrder = listOrder;
    }
}
