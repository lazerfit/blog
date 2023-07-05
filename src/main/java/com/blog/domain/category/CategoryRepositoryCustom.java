package com.blog.domain.category;

import com.blog.web.dto.CategoryEditRequestDto;
import com.blog.web.dto.PostsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepositoryCustom {

    Page<PostsResponseDto> getCategorizedPostsList(Pageable pageable,String q);

    void edit(Long categoryId,CategoryEditRequestDto categoryEditRequestDto);

}
