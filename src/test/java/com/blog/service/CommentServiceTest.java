package com.blog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.config.QdslConfig;
import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.comments.Comment;
import com.blog.domain.comments.CommentsRepository;
import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CategoryNotFound;
import com.blog.exception.CommentNotFound;
import com.blog.exception.PostNotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@Import(QdslConfig.class)
@AutoConfigureJson
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CommentServiceTest {

    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        commentsRepository.deleteAll();
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("save comment")
    void saveComment() {
        makePost("Spring", 1, "Spring in Action", "Spring in Action is a great");
        Post post = postsRepository.findById(1L).orElseThrow(PostNotFound::new);
        makeComment("user1", "comment1", null, post, "password1");

        Comment comment = commentsRepository.findById(1L).orElseThrow(CommentNotFound::new);

        assertThat(comment.getContent()).isEqualTo("comment1");
    }

    @Test
    @DisplayName("delete comment")
    void deleteComment() {
        makePost("Spring", 1, "Spring in Action", "Spring in Action is a great");
        Post post = postsRepository.findById(1L).orElseThrow(PostNotFound::new);
        makeComment("user1", "comment1", null, post, "password1");

        Comment comment = commentsRepository.findById(1L).orElseThrow(CommentNotFound::new);
        commentsRepository.delete(comment);

        assertThat(commentsRepository.existsById(1L)).isFalse();
    }

    @Test
    @DisplayName("update comment content")
    void updateCommentContent() {
        makePost("Spring", 1, "Spring in Action", "Spring in Action is a great");
        Post post = postsRepository.findById(1L).orElseThrow(PostNotFound::new);
        makeComment("user1", "comment1", null, post, "password1");

        Comment comment = commentsRepository.findById(1L).orElseThrow(CommentNotFound::new);
        comment.edit("new comment1");

        assertThat(comment.getContent()).isEqualTo("new comment1");
    }


    private void makePost(String categoryTitle, int listOrder, String postTitle,
        String postContent) {
        makeCategory(categoryTitle, listOrder);
        Category category = getCategory(categoryTitle);
        String tag = "[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";
        postsRepository.save(
            Post.builder()
                .title(postTitle)
                .content(postContent)
                .category(category)
                .tag(tag)
                .views(0L)
                .build()
        );
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

    private void makeComment(String username, String content, Comment parent, Post post, String password) {
        commentsRepository.save(
            Comment.builder()
              .username(username)
              .content(content)
              .parent(parent)
              .post(post)
              .password(password)
              .build()
        );
    }
}
