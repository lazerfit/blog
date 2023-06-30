package com.blog.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.PostsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class CategoryControllerTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리 저장")
    @WithMockUser(roles = {"ADMIN"})
    void test1() throws Exception {

        mockMvc.perform(post("/admin/setting/category")
                .param("title", "spring")
                .param("listOrder", "1")
            )
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }
}
