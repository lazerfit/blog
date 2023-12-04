package com.blog.web.dto.category;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CategoryAndPostCreatedDateResponse {

    private final Long id;
    private final int listOrder;
    private final String categoryTitle;
    private final LocalDateTime postCreatedDate;

    @QueryProjection
    public CategoryAndPostCreatedDateResponse(Long id, int listOrder, String categoryTitle,
        LocalDateTime postCreatedDate) {
        this.id = id;
        this.listOrder = listOrder;
        this.categoryTitle = categoryTitle;
        this.postCreatedDate = postCreatedDate;
    }
}
