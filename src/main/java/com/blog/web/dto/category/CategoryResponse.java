package com.blog.web.dto.category;

import com.blog.domain.category.Category;
import lombok.Getter;

@Getter
public class CategoryResponse {

    private final Long id;
    private final String title;
    private final Integer listOrder;

    public CategoryResponse(Category category) {
        this.id= category.getId();
        this.title= category.getTitle();
        this.listOrder= category.getListOrder();
    }

    public Category toEntity() {
        return Category.builder()
            .title(title)
            .listOrder(listOrder)
            .build();
    }
}
