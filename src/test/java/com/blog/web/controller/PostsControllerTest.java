package com.blog.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.posts.Posts;
import com.blog.domain.posts.PostsRepository;
import com.blog.web.dto.PostsSearchRequestDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
@SpringBootTest
class PostsControllerTest {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void tearDown() {
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("글 단건 조회")
    void singleSearch() throws Exception {
        Posts posts = postsRepository.save(Posts.builder()
            .title("title")
            .content("content")
            .author("author")
            .build());

        mockMvc.perform(get("/posts/" + posts.getId())
                .accept(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(posts.getTitle()))
            .andExpect(jsonPath("$.content").value(posts.getContent()));
    }

    @Test
    @DisplayName("글 다건 조회")
    void multipleSearch() throws Exception {
        IntStream.range(1, 30).forEach(
            i -> postsRepository.save(Posts.builder()
                .title("title" + i)
                .content("content" + i)
                .author("author" + i)
                .build())
        );

        PostsSearchRequestDto request = PostsSearchRequestDto.builder()
            .page(0)
            .build();

        mockMvc.perform(
                get("/posts?page={page}&size={size}", request.getPage(), request.getSize())
                    .accept(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("title29"))
            .andExpect(jsonPath("$[0].content").value("content29"));
    }

    @Test
    @DisplayName("업데이트")
    void update() throws Exception {
        Posts posts = postsRepository.save(Posts.builder()
            .title("title")
            .content("content")
            .author("author")
            .build());

        PostsUpdateRequestDto updateRequestDto = PostsUpdateRequestDto.builder()
            .title("modified title")
            .content("modified content")
            .build();

        mockMvc.perform(patch("/posts/{postsId}", posts.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestDto)))
            .andDo(print())
            .andExpect(status().isOk());

        mockMvc.perform(get("/posts/{postsId}", posts.getId())
                .accept(APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("modified title"))
            .andExpect(jsonPath("$.content").value("modified content"));
    }

    @Test
    @DisplayName("삭제")
    void delete() throws Exception {
        Posts posts = postsRepository.save(Posts.builder()
            .title("title")
            .content("content")
            .author("author")
            .build());

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", posts.getId())
                .accept(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("글 단건 조회 - 존재하지 않는 글")
    void searchNoExistPosts() throws Exception {

        mockMvc.perform(get("/posts/10")
                .accept(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.message").value("존재하지 않는 글입니다."));
    }

    @Test
    @DisplayName("글 저장 - 잘못된 요청")
    void submitWrongPosts() throws Exception {

        Posts posts = postsRepository.save(Posts.builder()
            .title("")
            .content("내용")
            .build());

        mockMvc.perform(post("/posts")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(posts)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validationErrors[:1].errorMessage").value("제목은 필수입니다."))
            .andExpect(jsonPath("$.validationErrors[:1].fieldName").value("title"));
    }

    @Test
    @DisplayName("get Categorized Posts")
    @WithMockUser(roles = "ADMIN")
    void getCategorizedPosts() throws Exception {
        Category category1 = new Category("Java", 1L);
        Category category2 = new Category("Spring", 2L);

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        postsRepository.save(Posts.builder()
            .title("제목1")
            .content("내용1")
            .category(category1)
            .build());

        postsRepository.save(Posts.builder()
            .title("제목2")
            .content("내용2")
            .category(category2)
            .build());

        mockMvc.perform(get("/posts/category?q={category}",category1.getTitle())
        ).andDo(print())
            .andExpect(status().isOk());
    }
}
