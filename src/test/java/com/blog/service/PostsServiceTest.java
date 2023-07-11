package com.blog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.config.QdslConfig;
import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.comments.Comment;
import com.blog.domain.comments.CommentsRepository;
import com.blog.domain.posts.Posts;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CommentNotFound;
import com.blog.exception.PostsNotFound;
import com.blog.web.dto.CommentsSaveRequestDto;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsResponseWithoutCommentDto;
import com.blog.web.dto.PostsSaveRequestDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(QdslConfig.class)
@AutoConfigureJson
class PostsServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    @BeforeEach
    void insertCategory() {
        Category category1 = new Category("Spring", 1);
        Category category2 = new Category("Java", 2);
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        String tagData = "[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";

        Category category = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        postsRepository.save(
            Posts.builder().title("제목").content("내용").category(category).tags(tagData).hit(0L)
                .build());

    }

    @AfterEach
    void tearDown() {
        postsRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("글 저장")
    void save() {

        Posts posts = postsRepository.save(Posts.builder()
            .title("제목")
            .content("내용")
            .build());

        Posts foundPosts = postsRepository.findById(posts.getId()).orElseThrow(PostsNotFound::new);

        assertThat(foundPosts.getTitle()).isEqualTo("제목");
        assertThat(foundPosts.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("글 수정")
    void edit() {
        Category category1 = categoryRepository.findCategoryByTitle("Spring").orElseThrow();
        Category category2 = categoryRepository.findCategoryByTitle("Java").orElseThrow();

        Posts posts = postsRepository.save(Posts.builder()
            .title("제목1")
            .content("내용1")
            .category(category1)
            .build());

        em.flush();
        em.clear();

        PostsUpdateRequestDto request = PostsUpdateRequestDto.builder()
            .title("수정된 제목")
            .content("수정된 내용")
            .category(category2)
            .build();

        postsRepository.edit(posts.getId(), request);

        Posts foundPosts = postsRepository.findById(posts.getId()).orElseThrow();

        assertThat(foundPosts.getCategory().getTitle()).isEqualTo("Java");
    }

    @Test
    @DisplayName("다건 조회")
    void multiSearch() {
        IntStream.range(1, 11).forEach(
            i -> postsRepository.save(Posts.builder()
                .title("제목" + i)
                .content("내용" + i)
                .build())
        );

        PageRequest request = PageRequest.of(0, 6);

        List<PostsResponseWithoutCommentDto> postsList = postsRepository.getPostsList(request).stream().toList();

        assertThat(postsList.get(0).getTitle()).isEqualTo("제목10");
        assertThat(postsList.get(0).getContent()).isEqualTo("내용10");
    }

    @Test
    @DisplayName("삭제")
    void delete() {

        Posts posts = postsRepository.save(Posts.builder()
            .title("제목")
            .content("내용")
            .build());

        Posts foundPosts = postsRepository.findById(posts.getId())
            .orElseThrow(PostsNotFound::new);

        postsRepository.delete(foundPosts);

        var afterFoundPost = postsRepository.findById(posts.getId());

        var ex = Assertions.assertThrows(PostsNotFound.class, () ->
            afterFoundPost.orElseThrow(PostsNotFound::new));
        Assertions.assertEquals("존재하지 않는 글입니다.", ex.getMessage());
    }

    @Test
    @DisplayName("게시글 카테고리 분류")
    void getCategorizedPosts() {
        Category category = new Category("Spring", 1);
        Category category2 = new Category("Java", 2);
        categoryRepository.save(category);
        categoryRepository.save(category2);

        postsRepository.save(Posts.builder()
            .title("제목1")
            .content("내용1")
            .category(category)
            .build());

        postsRepository.save(Posts.builder()
            .title("제목2")
            .content("내용2")
            .category(category2)
            .build());

        PageRequest pageRequest = PageRequest.of(0, 6);

        Page<PostsResponseWithCategoryDto> categorizedPosts = postsRepository.getCategorizedPosts(
            pageRequest,
            "Spring");

        assertThat(categorizedPosts.stream().toList().get(0).getTitle()).isEqualTo("제목1");
        assertThat(categorizedPosts.stream().toList()).hasSize(1);
    }

    @Test
    @DisplayName("태그 저장")
    void insertTags() {
        String tagData = "[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";

        Category category = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        PostsSaveRequestDto postsSaveRequestDto = new PostsSaveRequestDto("제목", "내용", category,
            tagData, 0L);

        postsRepository.save(postsSaveRequestDto.toEntity());

        List<Posts> all = postsRepository.findAll();

        assertThat(all.get(0).getTags()).isEqualTo("Spring,Java");
    }

    @Test
    @DisplayName("getTags as List")
    void getTagsAsList() {
        String tagData = "[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";

        Category category = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        PostsSaveRequestDto postsSaveRequestDto = new PostsSaveRequestDto("제목", "내용", category,
            tagData, 0L);

        postsRepository.save(postsSaveRequestDto.toEntity());

        List<Posts> all = postsRepository.findAll();

        Posts posts = postsRepository.findById(all.get(0).getId()).orElseThrow(PostsNotFound::new);
        String tags = posts.getTags();

        List<String> tagList = Stream.of(tags.split(",", -1)).toList();

        assertThat(tagList.get(0)).isEqualTo("Spring");
        assertThat(tagList.get(1)).isEqualTo("Java");
    }

    @Test
    @DisplayName("classified by tags")
    void getPostsByTags() {
        String tagData = "[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";

        Category category = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        IntStream.range(1, 30).forEach(i -> postsRepository.save(
            new PostsSaveRequestDto("제목" + i, "내용" + i, category, tagData, 0L).toEntity()));

//        postsRepository.save(postsSaveRequestDto.toEntity());

        PageRequest pageRequest = PageRequest.of(0, 6);

        Page<PostsResponseWithCategoryDto> postsByTags = postsRepository.getPostsByTags(pageRequest,
            "Spring");

        assertThat(postsByTags.stream().toList().get(0).getTitle()).isEqualTo("제목29");
        assertThat(postsByTags.stream().toList().get(0).getTags()).isEqualTo("Spring,Java");
    }

    @Test
    @DisplayName("조회수 체크")
    void hitTest() {
        String tagData = "[{\"value\":\"Spring\"},{\"value\":\"Java\"}]";

        Category category = categoryRepository.findCategoryByTitle("Spring").orElseThrow();

        postsRepository.save(
            Posts.builder().title("제목").content("내용").category(category).tags(tagData).hit(0L)
                .build());

        List<Posts> posts = postsRepository.findAll();

        Long hit = posts.get(0).getHit() + 1L;

        posts.get(0).updateHit(hit);

        Posts posts1 = postsRepository.findById(posts.get(0).getId()).orElseThrow();

        assertThat(posts1.getHit()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Tree Entity")
    void treeEntity() {
        Posts posts = postsRepository.findById(1L).orElseThrow();

        CommentsSaveRequestDto request = new CommentsSaveRequestDto("ddodi",
            "정말 좋은 글이네요", null, posts);

        commentsRepository.save(request.toEntity());

        Comment comment = commentsRepository.findById(1L).orElseThrow();

        assertThat(comment.getParent()).isNull();
        assertThat(comment.getUsername()).isEqualTo("ddodi");
        assertThat(comment.getContent()).isEqualTo("정말 좋은 글이네요");
        assertThat(comment.getPosts().getTitle()).isEqualTo("제목");

        Comment foundCommentByPostsId = commentsRepository.findByPostsId(1L);

        assertThat(foundCommentByPostsId.getUsername()).isEqualTo("ddodi");
    }

    @Test
    @DisplayName("get Post with Comment")
    void postWithComment() {

        Posts posts = postsRepository.findById(1L).orElseThrow();

        CommentsSaveRequestDto request = new CommentsSaveRequestDto("ddodi",
            "정말 좋은 글이네요", null, posts);

        commentsRepository.save(request.toEntity());
        Comment savedComment = commentsRepository.findById(1L).orElseThrow(CommentNotFound::new);

        CommentsSaveRequestDto sbuRequest = new CommentsSaveRequestDto("kim",
            "감사합니다.", savedComment, posts);

        commentsRepository.save(sbuRequest.toEntity());


        PostsResponseDto responseDto = postsRepository.findByIdWithQdsl(posts.getId());

        assertThat(responseDto.getComments().get(0).getContent()).isEqualTo("정말 좋은 글이네요");
        assertThat(responseDto.getComments().get(0).getChild().get(0).getContent()).isEqualTo(
            "감사합니다.");
    }
}
