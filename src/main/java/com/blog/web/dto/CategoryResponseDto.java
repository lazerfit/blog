package com.blog.web.dto;

import com.blog.domain.category.Category;
import lombok.Getter;

@Getter
public class CategoryResponseDto {

    private final Long id;
    private final String title;
    private final Integer listOrder;

    public CategoryResponseDto(Category category) {
        this.id= category.getId();
        this.title= category.getTitle();
        this.listOrder= category.getListOrder();
    }
}
