package com.blog.web.dto.category;

import com.blog.domain.category.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategorySaveRequest {

    private final String title;
    private final Integer listOrder;

    public Category toEntity() {
        return Category.builder()
            .title(title)
            .listOrder(listOrder)
            .build();
    }
}
