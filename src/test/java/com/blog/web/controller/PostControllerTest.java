package com.blog.web.controller;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CategoryNotFound;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
@WithMockUser(roles = "ADMIN")
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class PostControllerTest {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void saveMockPost() {
        makePost("spring",1,"Spring","content");
    }

    @AfterEach()
    void tearDown() {
        postsRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("글 저장")
    void savePost() throws Exception{

        String title = "제목";
        String content = "내용";
        String categoryTitle = "spring";
        String tag="[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";

        mockMvc.perform(post("/post/new")
                .with(csrf())
                .param("title", title)
                .param("content", content)
                .param("categoryTitle", categoryTitle)
                .param("tags", tag))
            .andDo(print())

        .andExpect(status().is3xxRedirection());
    }
    @Test
    @DisplayName("글 저장 - 실패")
    void savePostFail() throws Exception{

        String content = "내용1";
        String tags = "spring";
        String categoryTitle = "Spring";

        mockMvc.perform(post("/post/new")
                .with(csrf())
                .param("content",content)
                .param("tags",tags)
                .param("categoryTitle",categoryTitle))
            .andDo(print())
            .andExpect(content().json("{'message': '잘못된 요청입니다.'}"));
    }

    @Test
    @DisplayName("글 단건 조회")
    @WithMockUser(roles = "USER")
    void getPost() throws Exception {

        mockMvc.perform(get("/post/1"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("글 단건 조회 - 실패")
    @WithMockUser(roles = "USER")
    void gePostFail() throws Exception {

        mockMvc.perform(get("/post/2"))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(content().json("{'message':'존재하지 않는 글입니다.'}"));
    }

    @Test
    @DisplayName("업데이트")
    void update() throws Exception {

        mockMvc.perform(post("/post/edit/1")
                .with(csrf())
                .param("categoryTitle","spring")
                .param("title","제목")
                .param("content","내용")
                .param("tags","spring")
            .contentType(APPLICATION_FORM_URLENCODED_VALUE))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("삭제")
    void delete() throws Exception {

        mockMvc.perform(post("/post/delete/1")
                .with(csrf()))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("카테고리 게시글 분류")
    void getCategorizedPosts() throws Exception {

        PageRequest pageRequest = PageRequest.of(0, 6);

        mockMvc.perform(get("/post/category")
                .content(objectMapper.writeValueAsString(pageRequest))
                .param("q", "Spring"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("태그로 게시글 분류")
    void getPostsByTags() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 6);

        mockMvc.perform(get("/tag")
                .param("q", "Spring"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private void makePost(String categoryTitle,int listOrder,String postTitle,String postContent) {
        makeCategory(categoryTitle,listOrder);
        Category category = getCategory(categoryTitle);
        String tag="[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";
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
}
