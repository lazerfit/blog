package com.blog.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CategoryNotFound;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CategoryServiceTest {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    @Test
    void 사이드바_카테고리_불러오기() {
        makeCategory("java",2);

        Category category = categoryRepository.findCategoryByTitle("java")
            .orElseThrow(CategoryNotFound::new);

        postsRepository.save(Post.builder()
            .content("내용")
            .title("제목")
            .views(0L)
            .category(category)
            .tag("[{\"value\":\"Spring\"},{\"value\":\"Java\"}]")
            .build()
        );

        var allCategoryAndPostCreatedDate = categoryRepository.getAllCategoryAndPostCreatedDate();

        assertThat(allCategoryAndPostCreatedDate.get(1).getPostCreatedDate().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(allCategoryAndPostCreatedDate.get(1).getCategoryTitle()).isEqualTo("java");
        assertThat(allCategoryAndPostCreatedDate.get(1).getListOrder()).isEqualTo(2);
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
