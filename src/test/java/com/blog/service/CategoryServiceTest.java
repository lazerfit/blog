package com.blog.service;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CategoryNotFound;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void insertCategories() {
        Category category1 = new Category("Spring", 1);
        Category category2 = new Category("Java", 2);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
    }

    @AfterEach
    void tearDown() {
        postsRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategoryById() {

        Category category = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        categoryRepository.deleteById(category.getId());

        Optional<Category> categoryByTitle = categoryRepository.findCategoryByTitle("spring");

        Assertions.assertThrows(CategoryNotFound.class,
            () -> categoryByTitle.orElseThrow(CategoryNotFound::new)
            , "존재하지 않는 카테고리입니다.");
    }
}
