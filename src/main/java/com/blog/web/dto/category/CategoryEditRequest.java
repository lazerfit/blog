package com.blog.web.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryEditRequest {

    private final Long id;
    @NotBlank(message = "제목은 필수입니다.")
    private final String title;
    @NotBlank(message = "정렬 순서는 필수입니다.")
    private final Integer listOrder;

    @Builder
    public CategoryEditRequest(String title, Integer listOrder, Long id) {
        this.title = title;
        this.listOrder = listOrder;
        this.id=id;
    }
}
