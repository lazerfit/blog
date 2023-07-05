package com.blog.domain.category;

import com.blog.config.QdslConfig;
import com.blog.domain.posts.Posts;
import com.blog.domain.posts.PostsRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QdslConfig.class)
class CategoryRepositoryImplTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    void test1() {

        categoryRepository.save(Category.builder()
            .title("Java")
            .listOrder(1)
            .build());

        em.flush();
        em.clear();

        Posts posts = postsRepository.save(Posts.builder()
            .title("제목1")
            .content("내용1")
            .category(categoryRepository.findCategoryByTitle("Java").get())
            .build());

        Assertions.assertThat(posts.getCategory().getTitle()).isEqualTo("Java");
    }

}
