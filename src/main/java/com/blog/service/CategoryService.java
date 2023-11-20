package com.blog.service;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.exception.CategoryNotFound;
import com.blog.web.dto.category.CategoryEditRequest;
import com.blog.web.dto.category.CategoryResponse;
import com.blog.web.dto.category.CategorySaveRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void save(CategorySaveRequest requestDto) {
        categoryRepository.save(requestDto.toEntity());
    }

    @Cacheable(value = "categoryCache", unless = "#result==null")
    public CategoryResponse findCategoryByTitle(String title) {
        return categoryRepository.findCategoryByTitle(title).map(CategoryResponse::new).orElseThrow();
    }

    public List<CategoryResponse> findAllCategory() {
        return categoryRepository.findAll(Sort.by(Sort.DEFAULT_DIRECTION,"listOrder"))
            .stream().map(CategoryResponse::new).toList();
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void edit(Long categoryId, CategoryEditRequest form) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(CategoryNotFound::new);
        category.edit(form.getTitle(), form.getListOrder());
    }
}
