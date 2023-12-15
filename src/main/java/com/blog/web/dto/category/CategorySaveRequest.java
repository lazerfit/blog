package com.blog.web.dto.category;

import com.blog.domain.category.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategorySaveRequest {

    @NotBlank
    private final String title;
    @NotNull
    private final Integer listOrder;

    public Category toEntity() {
        return Category.builder()
            .title(title)
            .listOrder(listOrder)
            .build();
    }
}
