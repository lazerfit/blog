package com.blog.web.controller;

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
import com.blog.web.form.PostCreateForm;
import com.blog.web.form.PostEditForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
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

        PostCreateForm form = new PostCreateForm();
        form.setTitle(title);
        form.setContent(content);
        form.setCategoryTitle(categoryTitle);
        form.setTags(tag);

        mockMvc.perform(post("/post/new", 1L)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
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

        String title="제목1";
        String content = "내용1";
        String tags = "spring";
        String categoryTitle = "spring";

        PostEditForm form = PostEditForm.builder()
            .content(content)
            .tags(tags)
            .categoryTitle(categoryTitle)
            .title(title)
            .build();

        Long postId = postsRepository.findAll().get(0).getId();

        mockMvc.perform(post("/post/edit/"+postId, 1L)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
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
