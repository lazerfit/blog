package com.blog.domain.posts;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.config.QdslConfig;
import com.blog.domain.category.CategoryRepository;
import com.blog.web.dto.PostsResponse;
import jakarta.persistence.EntityManager;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(QdslConfig.class)
class PostRepositoryImplTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void insertMockData() {
        IntStream.range(1,20).forEach(i ->
            postsRepository.save(Post.builder()
                .title("제목"+i)
                .content("내용"+i)
                .build()));
    }

    @AfterEach
    void teardown() {
        postsRepository.deleteAll();
        em.clear();
    }

    @Test
    void test1() {
        postsRepository.save(Post.builder()
            .title("테스트용 제목입니다.")
            .content("내용입니다.")
            .build());
        PageRequest pageRequest = PageRequest.of(0, 6);
        Page<PostsResponse> searchedPosts = postsRepository.getPostsListByKeyword(pageRequest, "테스트");

        assertThat(searchedPosts.stream().toList()).hasSize(1);
        assertThat(searchedPosts.stream().toList().get(0).getTitle()).isEqualTo("테스트용 제목입니다.");
    }
}
