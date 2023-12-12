package com.blog.domain.category;

import com.blog.web.dto.category.CategoryAndPostCreatedDateResponse;
import java.util.List;

public interface CategoryRepositoryCustom {


    List<CategoryAndPostCreatedDateResponse> getAllCategoryAndPostCreatedDate();


}
