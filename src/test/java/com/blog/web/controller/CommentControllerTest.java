package com.blog.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.comments.Comment;
import com.blog.domain.comments.CommentsRepository;
import com.blog.domain.posts.Posts;
import com.blog.domain.posts.PostsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
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

    @BeforeEach
    void insertCategories() {
        Category category1 = new Category("Java", 1);
        Category category2 = new Category("Spring", 2);

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        String tagData = "[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";

        Category category = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        postsRepository.save(
            Posts.builder().title("제목").content("내용").category(category).tags(tagData).hit(0L)
                .build());
    }

    @AfterEach()
    void tearDown() {
        postsRepository.deleteAll();
        categoryRepository.deleteAll();
    }
    @Test
    @DisplayName("Comment Delete")
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void deleteComment() throws Exception {

        Posts posts = postsRepository.findById(1L).orElseThrow();

        Comment comment = Comment.builder()
            .password("1234")
            .parent(null)
            .posts(posts)
            .username("s")
            .content("ss")
            .build();

        Map<String,Long> commentId=new HashMap<>();
        Map<String,Long> postId=new HashMap<>();
        List<Map<String,Long>> commentInfo=new ArrayList<>();

        commentId.put("commentId",1L);
        commentId.put("postId",1L);

        commentInfo.add(commentId);
        commentInfo.add(postId);

        commentsRepository.save(comment);

        System.out.println("--------------"+commentInfo);
        mockMvc.perform(post("/posts/comment/delete")
                .param("commentId","1")
                .param("postId","1"))
            .andDo(print());
    }
}
