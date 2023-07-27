package com.blog.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CategoryNotFound;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        postsRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리 생성")
    void createCategory() {
        makeCategory("Spring", 1);
        Category category = getCategory("Spring");

        assertThat(category.getTitle()).isEqualTo("Spring");
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategoryById() {
        makeCategory("Spring", 1);
        categoryRepository.deleteAll();

        boolean existsById = categoryRepository.existsById(1L);
        assertThat(existsById).isFalse();
    }

    @Test
    @DisplayName("카테고리 업데이트")
    @Transactional
    void updateCategory() {
        makeCategory("Spring", 1);

        getCategory("Spring").edit("Spring2", 2);
        Category category = categoryRepository.findCategoryByTitle("Spring2")
            .orElseThrow(CategoryNotFound::new);

        assertThat(category.getTitle()).isEqualTo("Spring2");
    }

    private void makeCategory(String categoryTitle, int listOrder) {
        Category category = new Category(categoryTitle, listOrder);
        categoryRepository.save(category);
    }
    private Category getCategory(String categoryTitle) {
        return categoryRepository.findCategoryByTitle(categoryTitle)
            .orElseThrow(CategoryNotFound::new);
    }
}
