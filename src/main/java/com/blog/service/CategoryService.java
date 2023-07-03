package com.blog.service;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.exception.CategoryNotFound;
import com.blog.web.dto.CategoryCreateRequestDto;
import com.blog.web.dto.PostsResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Page<PostsResponseDto> getCategorizedPosts(Pageable pageable,String q) {
        return categoryRepository.getCategorizedPostsList(pageable,q);
    }

    @Transactional
    public void save(CategoryCreateRequestDto requestDto) {
        categoryRepository.save(requestDto.toEntity());
    }

    public Category getCategoryByTitle(String title) {
        return categoryRepository.findCategoryByTitle(title).orElseThrow(CategoryNotFound::new);
    }

    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }
}
