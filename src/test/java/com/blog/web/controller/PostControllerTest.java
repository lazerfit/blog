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
import com.blog.domain.comments.Comment;
import com.blog.domain.comments.CommentsRepository;
import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.web.dto.PostsSaveRequestDto;
import com.blog.web.dto.PostsSearchRequestDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import com.blog.web.form.CommentForm;
import com.blog.web.form.CreatePostsForm;
import com.blog.web.form.EditPostsForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private EntityManager em;

    @BeforeEach
    void insertCategories() {
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
    @DisplayName("글 단건 조회")
    void singleSearch() throws Exception {
        Post post = postsRepository.save(Post.builder()
            .title("title")
            .content("content")
            .build());

        mockMvc.perform(get("/posts/" + post.getId())
                .accept(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(post.getTitle()))
            .andExpect(jsonPath("$.content").value(post.getContent()));
    }

    @Test
    @DisplayName("글 다건 조회")
    void multipleSearch() throws Exception {
        IntStream.range(1, 30).forEach(
            i -> postsRepository.save(Post.builder()
                .title("title" + i)
                .content("content" + i)
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
        Post post = postsRepository.save(Post.builder()
            .title("title")
            .content("content")
            .build());

        PostsUpdateRequestDto updateRequestDto = PostsUpdateRequestDto.builder()
            .title("modified title")
            .content("modified content")
            .build();

        mockMvc.perform(patch("/posts/{postsId}", post.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestDto)))
            .andDo(print())
            .andExpect(status().isOk());

        mockMvc.perform(get("/posts/{postsId}", post.getId())
                .accept(APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("modified title"))
            .andExpect(jsonPath("$.content").value("modified content"));
    }

    @Test
    @DisplayName("삭제")
    void delete() throws Exception {
        Post post = postsRepository.save(Post.builder()
            .title("title")
            .content("content")
            .build());

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
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

        Post post = postsRepository.save(Post.builder()
            .title("")
            .content("내용")
            .build());

        mockMvc.perform(post("/posts")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)))
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
        Category category1 = new Category("Java", 1);
        Category category2 = new Category("Spring", 2);

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        postsRepository.save(Post.builder()
            .title("제목1")
            .content("내용1")
            .category(category1)
            .build());

        postsRepository.save(Post.builder()
            .title("제목2")
            .content("내용2")
            .category(category2)
            .build());

        mockMvc.perform(get("/posts/category?q={category}", category1.getTitle())
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("edit category on Posts")
    @WithMockUser(roles = "ADMIN")
    void editCategoryOnPosts() throws Exception {

        Category foundCategory1 = categoryRepository.findCategoryByTitle("Java").orElseThrow();
        Category foundCategory2 = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        Post post = postsRepository.save(Post.builder()
            .title("제목1")
            .content("내용1")
            .category(foundCategory1)
            .build());

        EditPostsForm editPostsForm = new EditPostsForm("수정된 제목", "수정된 내용", foundCategory1);

        mockMvc.perform(post("/posts/edit/{postId}", post.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editPostsForm)))
            .andDo(print())
            .andExpect(status().is3xxRedirection());

    }

    @Test
    @DisplayName("Save Tags")
    @WithMockUser(roles = "ADMIN")
    void saveTags() throws Exception {

        Category foundCategory1 = categoryRepository.findCategoryByTitle("Java").orElseThrow();

        String tagData = "[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";

        CreatePostsForm createPostsForm = new CreatePostsForm();
        createPostsForm.setTitle("제목");
        createPostsForm.setContent("내용");
        createPostsForm.setCategoryTitle("Spring");
        createPostsForm.setTags(tagData);

        mockMvc.perform(post("/posts/new")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPostsForm)))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("태그로 게시글 분류")
    @WithMockUser(roles = "ADMIN")
    void getPostsByTags() throws Exception {

        String tagData = "[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";

        Category category = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        PostsSaveRequestDto postsSaveRequestDto = new PostsSaveRequestDto("제목", "내용", category,
            tagData, 0L);

        postsRepository.save(postsSaveRequestDto.toEntity());

        mockMvc.perform(get("/tag?q={tag}", "Spring"))
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
}
