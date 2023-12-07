package com.blog.service;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.exception.CategoryNotFound;
import com.blog.web.dto.category.CategoryAndPostCreatedDateResponse;
import com.blog.web.dto.category.CategoryEditRequest;
import com.blog.web.dto.category.CategoryResponse;
import com.blog.web.dto.category.CategorySaveRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    @CacheEvict(value = {"categoryList","categoryCache"},allEntries = true)
    public void save(CategorySaveRequest requestDto) {
        categoryRepository.save(requestDto.toEntity());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "categoryCache", unless = "#result==null")
    public CategoryResponse findCategoryByTitle(String title) {
        return categoryRepository.findCategoryByTitle(title).map(CategoryResponse::new).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAllCategory() {
        return categoryRepository.findAll(Sort.by(Sort.DEFAULT_DIRECTION,"listOrder"))
            .stream().map(CategoryResponse::new).toList();
    }

    @Transactional
    @CacheEvict(value = {"categoryList","categoryCache"},allEntries = true)
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = {"categoryList","categoryCache"},allEntries = true)
    public void edit(Long categoryId, CategoryEditRequest form) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(CategoryNotFound::new);
        category.edit(form.getTitle(), form.getListOrder());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "categoryList")
    public List<CategoryAndPostCreatedDateResponse> getAllCategoryAndPostCreatedDate() {
        return categoryRepository.getAllCategoryAndPostCreatedDate();
    }
}
