package com.blog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.config.QdslConfig;
import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.Posts;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.PostsNotFound;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(QdslConfig.class)
class PostsServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void insertCategory() {
        Category category1 = new Category("Spring", 1L);
        Category category2 = new Category("Java", 2L);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
    }

    @AfterEach
    void tearDown() {
        postsRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("글 저장")
    void save() {

        Posts posts = postsRepository.save(Posts.builder()
            .title("제목")
            .content("내용")
            .build());

        Posts foundPosts = postsRepository.findById(posts.getId()).orElseThrow(PostsNotFound::new);

        assertThat(foundPosts.getTitle()).isEqualTo("제목");
        assertThat(foundPosts.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("글 수정")
    void edit() {
        Category category1 = categoryRepository.findCategoryByTitle("Spring").orElseThrow();
        Category category2 = categoryRepository.findCategoryByTitle("Java").orElseThrow();

        Posts posts = postsRepository.save(Posts.builder()
            .title("제목1")
            .content("내용1")
            .category(category1)
            .build());

        em.flush();
        em.clear();

        PostsUpdateRequestDto request = PostsUpdateRequestDto.builder()
            .title("수정된 제목")
            .content("수정된 내용")
            .category(category2)
            .build();

        postsRepository.edit(posts.getId(),request);

        Posts foundPosts = postsRepository.findById(posts.getId()).orElseThrow();

        assertThat(foundPosts.getCategory().getTitle()).isEqualTo("Java");
    }

    @Test
    @DisplayName("다건 조회")
    void multiSearch() {
        IntStream.range(1, 11).forEach(
            i -> postsRepository.save(Posts.builder()
                .title("제목" + i)
                .content("내용" + i)
                .build())
        );

        PageRequest request = PageRequest.of(0, 6);

        List<PostsResponseDto> postsList = postsRepository.getPostsList(request).stream().toList();

        assertThat(postsList.get(0).getTitle()).isEqualTo("제목10");
        assertThat(postsList.get(0).getContent()).isEqualTo("내용10");
    }

    @Test
    @DisplayName("삭제")
    void delete() {

        Posts posts = postsRepository.save(Posts.builder()
            .title("제목")
            .content("내용")
            .build());

        Posts foundPosts = postsRepository.findById(posts.getId())
            .orElseThrow(PostsNotFound::new);

        postsRepository.delete(foundPosts);

        var afterFoundPost = postsRepository.findById(posts.getId());

        var ex = Assertions.assertThrows(PostsNotFound.class, () ->
            afterFoundPost.orElseThrow(PostsNotFound::new));
        Assertions.assertEquals("존재하지 않는 글입니다.",ex.getMessage());
    }

    @Test
    @DisplayName("게시글 카테고리 분류")
    void getCategorizedPosts() {
        Category category = new Category("Spring", 1L);
        Category category2 = new Category("Java", 2L);
        categoryRepository.save(category);
        categoryRepository.save(category2);

        postsRepository.save(Posts.builder()
            .title("제목1")
            .content("내용1")
            .category(category)
            .build());

        postsRepository.save(Posts.builder()
            .title("제목2")
            .content("내용2")
            .category(category2)
            .build());

        PageRequest pageRequest = PageRequest.of(0, 6);

        Page<PostsResponseWithCategoryDto> categorizedPosts = postsRepository.getCategorizedPosts(pageRequest,
            "Spring");

        assertThat(categorizedPosts.stream().toList().get(0).getTitle()).isEqualTo("제목1");
        assertThat(categorizedPosts.stream().toList()).hasSize(1);
    }
}
