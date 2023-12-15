package com.blog.web.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.PostsRepository;
import com.blog.web.form.CategoryEditForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CategoryControllerTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리 저장")
    @WithMockUser(roles = {"ADMIN"})
    void saveCategory() throws Exception {

        CategoryEditForm form = new CategoryEditForm();
        form.setTitle("spring");
        form.setListOrder(1);

        mockMvc.perform(post("/admin/setting/category/edit")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
            )
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("카테고리 삭제")
    @WithMockUser(roles = {"ADMIN"})
    void deleteCategoryById() throws Exception {

        makeCategory("spring",1);

        mockMvc.perform(post("/admin/setting/category/delete/{categoryId}", 1L)
                .with(csrf()))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("카테고리 업데이트")
    @WithMockUser(roles = {"ADMIN"})
    void updateCategory() throws Exception {

        Category category = makeCategory("spring", 1);

        CategoryEditForm categoryEditForm = new CategoryEditForm();
        categoryEditForm.setTitle("spring");
        categoryEditForm.setListOrder(2);
        categoryEditForm.setId(category.getId());

        mockMvc.perform(post("/admin/setting/category/edit")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryEditForm)))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    private Category makeCategory(String categoryTitle, int listOrder) {
        Category category = Category.builder()
            .title(categoryTitle)
            .listOrder(listOrder)
            .build();
        return categoryRepository.save(category);
    }
}
