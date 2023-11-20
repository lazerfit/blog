package com.blog.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CategoryNotFound;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CategoryServiceTest {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void makeDummyCategory() {
        makeCategory("Spring", 1);
    }

    @AfterEach
    void tearDown() {
        postsRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리 생성")
    void createCategory() {
        Category category = getCategory("Spring");

        assertThat(category.getTitle()).isEqualTo("Spring");
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategoryById() {
        categoryRepository.deleteAll();

        boolean existsById = categoryRepository.existsById(1L);
        assertThat(existsById).isFalse();
    }

    @Test
    @DisplayName("카테고리 업데이트")
    @Transactional
    void updateCategory() {
        getCategory("Spring").edit("Spring2", 2);
        Category category = categoryRepository.findCategoryByTitle("Spring2")
            .orElseThrow(CategoryNotFound::new);

        assertThat(category.getTitle()).isEqualTo("Spring2");
    }

    private Category getCachedCategory(String title) {
        return Optional.ofNullable(cacheManager.getCache("category")).map(c ->
            c.get(title, Category.class)).orElseThrow();
    }

    private void makeCategory(String categoryTitle, int listOrder) {
        Category category = Category.builder()
            .title(categoryTitle)
            .listOrder(listOrder)
            .build();
        categoryRepository.save(category);
    }
    private Category getCategory(String categoryTitle) {
        return categoryRepository.findCategoryByTitle(categoryTitle)
            .orElseThrow(CategoryNotFound::new);
    }
}
