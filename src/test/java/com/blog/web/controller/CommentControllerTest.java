package com.blog.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.comments.Comment;
import com.blog.domain.comments.CommentsRepository;
import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.web.form.CommentForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void insertCategories() {
        Category category1 = Category.builder()
            .title("Java")
            .listOrder(1)
            .build();
        Category category2 = Category.builder()
            .title("Spring")
            .listOrder(2)
            .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        String tagData = "[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";

        Category category = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        postsRepository.save(
            Post.builder().title("제목").content("내용").category(category).tag(tagData).views(0L)
                .build());
    }

    @AfterEach()
    void tearDown() {
        postsRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Comment save")
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void saveComment() throws Exception {

        Post post = postsRepository.findById(1L).orElseThrow();

        CommentForm commentForm = new CommentForm();
        commentForm.setUsername("s");
        commentForm.setContent("ss");
        commentForm.setParentId(null);
        commentForm.setPostId(1L);
        commentForm.setPassword("1234");

        mockMvc.perform(post("/post/comment/new")
                .content(objectMapper.writeValueAsString(commentForm))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        assertThat(commentsRepository.count()).isEqualTo(1);
        assertThat(commentsRepository.findAll().get(0).getUsername()).isEqualTo("s");
        assertThat(commentsRepository.findAll().get(0).getContent()).isEqualTo("ss");
    }


    @Test
    @DisplayName("Comment Delete by Admin")
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void deleteComment() throws Exception {

        Post post = postsRepository.findById(1L).orElseThrow();

        Comment comment = Comment.builder()
            .password("1234")
            .parent(null)
            .post(post)
            .username("s")
            .content("ss")
            .build();

        commentsRepository.save(comment);

        mockMvc.perform(post("/post/admin/comment/delete")
                .param("commentId", "1")
                .param("postId", "1"))
            .andDo(print())
            .andExpect(status().isOk());

        assertThat(commentsRepository.count()).isZero();
    }

    @Test
    @DisplayName("Comment Delete by USER")
    @WithMockUser(roles = "USER")
    @Transactional
    void deleteCommentByUser() throws Exception {

        Post post = postsRepository.findById(1L).orElseThrow();

        Comment comment = Comment.builder()
            .password(passwordEncoder.encode("1234"))
            .parent(null)
            .post(post)
            .username("s")
            .content("ss")
            .build();

        commentsRepository.save(comment);

        mockMvc.perform(post("/post/comment/manage/delete")
                .param("password","123")
                .param("commentId", "1"))
            .andDo(print())
            .andExpect(status().isOk());

        assertThat(commentsRepository.count()).isZero();
    }

    @Test
    @DisplayName("comment edit")
    @WithMockUser(roles = "USER")
    @Transactional
    void editComment() throws Exception {

        Post post = postsRepository.findById(1L).orElseThrow();

        Comment comment = Comment.builder()
            .password(passwordEncoder.encode("1234"))
            .parent(null)
            .post(post)
            .username("s")
            .content("ss")
            .build();

        commentsRepository.save(comment);

        mockMvc.perform(MockMvcRequestBuilders.post("/post/comment/manage/edit")
                .param("id","1")
                .param("password","1234")
                .param("content","멍"))
            .andDo(print())
            .andExpect(status().isOk());

        assertThat(commentsRepository.findAll().get(0).getContent()).isEqualTo("멍");
    }
}
