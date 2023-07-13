package com.blog.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.blog.config.QdslConfig;
import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.comments.Comment;
import com.blog.domain.comments.CommentsRepository;
import com.blog.domain.posts.Posts;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CommentNotFound;
import com.blog.web.dto.CommentEditRequest;
import com.blog.web.form.CommentEditForm;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QdslConfig.class)
@AutoConfigureJson
class CommentServiceTest {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void insertPosts() {
        Category category1 = new Category("Spring", 1);
        Category category2 = new Category("Java", 2);
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        String tagData = "[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";

        Category category = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        postsRepository.save(
            Posts.builder().title("제목").content("내용").category(category).tags(tagData).hit(0L)
                .build());
    }

    @AfterEach
    void tearDown() {
        commentsRepository.deleteAll();
    }

    @Test
    @DisplayName("delete Comment")
    void delete() {

        Posts posts = postsRepository.findById(1L).orElseThrow();

        Comment comment = Comment.builder()
            .username("kim")
            .content("gg")
            .posts(posts)
            .parent(null)
            .password("1234")
            .build();

        commentsRepository.save(comment);

        Comment savedComment = commentsRepository.findById(1L).orElseThrow();

        commentsRepository.delete(savedComment);

        Optional<Comment> deletedComment = commentsRepository.findById(1L);

        assertThrows(CommentNotFound.class, () ->
            deletedComment.orElseThrow(CommentNotFound::new));
    }

    @Test
    @DisplayName("edit comment")
    void edit() {
        Posts posts = postsRepository.findById(1L).orElseThrow();

        Comment comment = Comment.builder()
            .username("kim")
            .content("gg")
            .posts(posts)
            .parent(null)
            .password("1234")
            .build();

        commentsRepository.save(comment);

        Comment savedComment = commentsRepository.findById(1L).orElseThrow();

        CommentEditForm form = new CommentEditForm();
        form.setId(1L);
        form.setContent("멍멍");

        CommentEditRequest request = new CommentEditRequest(form);

        savedComment.edit(request.getContent());
    }
}
