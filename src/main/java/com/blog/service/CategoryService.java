package com.blog.service;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.exception.CategoryNotFound;
import com.blog.web.dto.category.CategoryEditRequest;
import com.blog.web.dto.category.CategorySaveRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    public Category findCategoryByTitle(String title) {
        return categoryRepository.findCategoryByTitle(title).orElseThrow(CategoryNotFound::new);
    }

    // findAllCategory 빼고 나머지는 다 필요 없어 보임

    public List<Category> findAllCategory() {
        return categoryRepository.findAll(Sort.by(Sort.DEFAULT_DIRECTION,"listOrder"));
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
