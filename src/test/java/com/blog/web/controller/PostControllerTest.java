package com.blog.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.comments.Comment;
import com.blog.domain.comments.CommentsRepository;
import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CategoryNotFound;
import com.blog.web.dto.PostsSearchRequestDto;
import com.blog.web.form.CommentForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
@WithMockUser(roles = "ADMIN")
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @BeforeEach
    void setup() {
        Category category1 = new Category("Java", 1);
        Category category2 = new Category("Spring", 2);

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
    @DisplayName("글 저장")
    @WithMockUser(roles = "ADMIN")
    void savePost() throws Exception{

        String title = "제목1";
        String content = "내용1";
        String tags = "spring";
        String categoryTitle = "Spring";

        mockMvc.perform(post("/post/new")
                .param("title",title)
                .param("content",content)
                .param("tags",tags)
                .param("categoryTitle",categoryTitle))
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
                .param("content",content)
                .param("tags",tags)
                .param("categoryTitle",categoryTitle))
            .andDo(print())
            .andExpect(content().json("{'message': '잘못된 요청입니다.'}"));
    }

    @Test
    @DisplayName("글 단건 조회")
    @WithMockUser(roles = "ADMIN")
    void getPost() throws Exception {

        mockMvc.perform(get("/post/1"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("글 단건 조회 - 실패")
    @WithMockUser(roles = "ADMIN")
    void gePostFail() throws Exception {

        mockMvc.perform(get("/post/2"))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(content().json("{'message':'존재하지 않는 글입니다.'}"));
    }

    @Test
    @DisplayName("글 다건 조회")
    @WithMockUser(roles = "ADMIN")
    void multipleSearch() throws Exception {

        Category category = categoryRepository.findById(1L).orElseThrow(CategoryNotFound::new);

        createPosts(category);

        PostsSearchRequestDto request = PostsSearchRequestDto.builder()
            .page(0)
            .build();

        mockMvc.perform(
                get("/?page={page}&size={size}", request.getPage(), request.getSize())
                    .accept(APPLICATION_JSON))
            .andDo(print())
            .andExpect(result -> {
                String contentAsString = result.getResponse().getContentAsString();
                contentAsString.contains("title29");
            });
    }

    @Test
    @DisplayName("업데이트")
    void update() throws Exception {

        mockMvc.perform(post("/post/edit/1")
                .param("title", "수정된 제목")
                .param("content", "수정된 내용")
                .param("categoryTitle", "Spring"))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("삭제")
    void delete() throws Exception {

        mockMvc.perform(post("/post/delete/1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("get Categorized Posts")
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
    void getPostsByTags() throws Exception {

        mockMvc.perform(get("/tag")
                .param("q", "Spring"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 저장")
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void saveComment() throws Exception {

        List<Post> all = postsRepository.findAll();

        CommentForm commentForm = new CommentForm();
        commentForm.setUsername("sg");
        commentForm.setContent("정말 멋집 글이네요");
        commentForm.setPostId(all.get(0).getId());
        commentForm.setParentId(null);
        commentForm.setPassword("1234");

        mockMvc.perform(post("/posts/1/comment")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentForm)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("대댓글 저장")
    void saveSubComment() throws Exception {

        List<Post> all = postsRepository.findAll();

        commentsRepository.save(Comment.builder()
            .username("sg")
            .content("정말 좋은 글이네요")
            .parent(null)
            .post(all.get(0))
            .build());

        List<Comment> allComments = commentsRepository.findAll();
        Long parentId = allComments.get(0).getId();

        CommentForm SubCommentForm = new CommentForm();
        SubCommentForm.setUsername("kim");
        SubCommentForm.setContent("정말 감사합니다.");
        SubCommentForm.setPostId(all.get(0).getId());
        SubCommentForm.setParentId(parentId);

        mockMvc.perform(post("/posts/1/comment")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(SubCommentForm)))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("게시글 가져오기 - 댓글 포함")
    @Transactional
    void getPostsWithComment() throws Exception {

        List<Post> all = postsRepository.findAll();

        commentsRepository.save(Comment.builder()
            .username("sg")
            .content("정말 좋은 글이네요")
            .parent(null)
            .post(all.get(0))
            .password("1234")
            .build());

        mockMvc.perform(get("/posts/1"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 수정 - 수정 폼")
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void editForm() throws Exception{

        // 수정 폼에 원래 게시글 정보 전달

        mockMvc.perform(get("/post/edit/1"))
            .andDo(print())
            .andExpect(status().isOk());

    }

    private void createPosts(Category category) {
        IntStream.range(1, 30).forEach(
            i -> postsRepository.save(Post.builder()
                .title("title" + i)
                .content("content" + i)
                .views(0L)
                .tag("spring")
                .category(category)
                .build())
        );
    }
}
