package com.blog.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CategoryNotFound;
import com.blog.web.form.CategoryEditForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    void insertCategories() {
        Category category1 = new Category("Spring", 1);
        Category category2 = new Category("Java", 2);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
    }

    @AfterEach
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

    @Test
    @DisplayName("카테고리 삭제")
    @WithMockUser(roles = {"ADMIN"})
    void deleteCategoryById() throws Exception {

        Category category = categoryRepository.findCategoryByTitle("Spring")
            .orElseThrow(CategoryNotFound::new);

        mockMvc.perform(post("/admin/setting/category/delete/{categoryId}", category.getId()))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("카테고리 업데이트")
    @WithMockUser(roles = {"ADMIN"})
    void updateCategory() throws Exception {

        Category category = categoryRepository.findCategoryByTitle("Spring")
            .orElseThrow(CategoryNotFound::new);

        CategoryEditForm categoryEditForm = new CategoryEditForm();
        categoryEditForm.setTitle("고양이");
        categoryEditForm.setListOrder(1);

        mockMvc.perform(post("/admin/setting/category/edit/{categoryId}", category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryEditForm)))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }
}
