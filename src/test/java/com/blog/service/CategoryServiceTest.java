package com.blog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CategoryNotFound;
import com.blog.web.dto.PostsResponse;
import com.blog.web.form.CategoryEditForm;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
    void test1() {
        Category category1 = new Category("Spring", 1);
        Category category2 = new Category("Java", 2);
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        postsRepository.save(Post.builder()
            .title("제목1")
            .content("내용1")
            .category(category1)
            .build());
        postsRepository.save(Post.builder()
            .title("제목2")
            .content("내용2")
            .category(category2)
            .build());

        PageRequest pageRequest = PageRequest.of(0, 6);

        Page<PostsResponse> categorizedByTitleOfSpring = categoryService.getCategorizedPosts(
            pageRequest, "Spring");
        Page<PostsResponse> categorizedByTitleOfJava = categoryService.getCategorizedPosts(
            pageRequest, "Java");

        assertThat(categorizedByTitleOfSpring.stream().toList().get(0).getTitle()).isEqualTo("제목1");
        assertThat(categorizedByTitleOfSpring).hasSize(1);

        assertThat(categorizedByTitleOfJava.stream().toList().get(0).getTitle()).isEqualTo("제목2");
        assertThat(categorizedByTitleOfJava).hasSize(1);
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

    @Test
    @DisplayName("카테고리 업데이트")
    void editCategory() {
        Category category = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        CategoryEditForm categoryEditForm = new CategoryEditForm();
        categoryEditForm.setTitle("고양이");
        categoryEditForm.setListOrder(1);

        categoryService.edit(category.getId(),categoryEditForm);

        Category category1 = categoryRepository.findById(1L).orElseThrow();

        assertThat(category1.getTitle()).isEqualTo("고양이");
    }
}
