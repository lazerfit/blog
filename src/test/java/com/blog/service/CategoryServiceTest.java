package com.blog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.Posts;
import com.blog.domain.posts.PostsRepository;
import com.blog.web.dto.PostsResponseDto;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        postsRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void test1() {
        Category category1 = new Category("Spring", 1L);
        Category category2 = new Category("Java", 2L);
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        postsRepository.save(Posts.builder()
            .title("제목1")
            .content("내용1")
            .category(category1)
            .build());
        postsRepository.save(Posts.builder()
            .title("제목2")
            .content("내용2")
            .category(category2)
            .build());

        PageRequest pageRequest = PageRequest.of(0, 6);

        Page<PostsResponseDto> categorizedByTitleOfSpring = categoryService.getCategorizedPosts(pageRequest, "Spring");
        Page<PostsResponseDto> categorizedByTitleOfJava = categoryService.getCategorizedPosts(pageRequest, "Java");

        assertThat(categorizedByTitleOfSpring.stream().toList().get(0).getTitle()).isEqualTo("제목1");
        assertThat(categorizedByTitleOfSpring).hasSize(1);

        assertThat(categorizedByTitleOfJava.stream().toList().get(0).getTitle()).isEqualTo("제목2");
        assertThat(categorizedByTitleOfJava).hasSize(1);
    }
}
