package com.blog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.config.QdslConfig;
import com.blog.domain.category.Category;
import com.blog.domain.category.CategoryRepository;
import com.blog.domain.comments.CommentsRepository;
import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CategoryNotFound;
import com.blog.exception.PostNotFound;
import com.blog.web.dto.posts.PostsResponse;
import com.blog.web.dto.posts.PostsUpdateRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DataJpaTest
@Import(QdslConfig.class)
@AutoConfigureJson
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class PostServiceTest {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    @BeforeEach
    void tearDown() {
        postsRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("글 저장")
    void save() {
        makePost("spring",1,"제목","내용");
        Post postsResponse = postsRepository.findById(1L).orElseThrow(PostNotFound::new);
        assertThat(postsResponse.getCategory().getTitle()).isEqualTo("spring");
    }

    @Test
    @DisplayName("글 업데이트")
    void update() {
        makePost("spring",1,"제목","내용");

        PostsUpdateRequest updateRequest = PostsUpdateRequest.builder()
            .title("제목 수정")
            .content("내용 수정")
            .build();
        Post post = postsRepository.findById(1L).orElseThrow();
        post.edit(updateRequest);

        Post foundPost = postsRepository.findById(1L).orElseThrow(PostNotFound::new);
        assertThat(foundPost.getTitle()).isEqualTo("제목 수정");
    }

    @Test
    @DisplayName("글 삭제")
    void delete() {
        makePost("spring",1,"제목","내용");

        postsRepository.deleteById(1L);

        boolean postExists = postsRepository.existsById(1L);
        assertThat(postExists).isFalse();
    }

    @Test
    @DisplayName("카테고리로 게시물 찾기")
    void findPostsByCategory() {
        makePost("spring",1,"제목","내용");
        makePost("java",2,"제목2","내용2");

        List<PostsResponse> java = postsRepository.getPostsSortedByCategory("java");

        assertThat(java.get(0).getTitle()).isEqualTo("제목2");
    }

    @Test
    @DisplayName("태그로 게시물 찾기")
    void findPostsByTag() {
        makePost("spring",1,"제목","내용");
        makePost("java",2,"제목2","내용2");

        PageRequest pageRequest = PageRequest.of(0, 6);

        Page<PostsResponse> posts = postsRepository.getPostsByTag(pageRequest, "Java");
        List<PostsResponse> postsList = posts.stream().toList();
        assertThat(postsList.get(0).getTitle()).isEqualTo("제목2");
        assertThat(postsList.get(1).getTitle()).isEqualTo("제목");
    }

    @Test
    @DisplayName("조회수 증가")
    void addViews() {
        makePost("spring",1,"제목","내용");

        Post post = postsRepository.findById(1L).orElseThrow(PostNotFound::new);
        post.addViews(1L);

        assertThat(post.getViews()).isEqualTo(1L);
    }

    @Test
    @DisplayName("인기 게시글 조회")
    void getPopularPosts() {
        makePost("spring",1,"제목","내용");
        makePost("java",2,"제목2","내용2");

        Post post = postsRepository.findById(2L).orElseThrow(PostNotFound::new);
        post.addViews(2L);

        List<PostsResponse> popularPosts = postsRepository.getPopularPosts();

        assertThat(popularPosts.get(0).getTitle()).isEqualTo("제목2");
    }

    @Test
    @DisplayName("검색어로 게시글 찾기")
    void getPostsByKeyword() {
        makePost("spring",1,"제목","내용");
        makePost("java",2,"고양이","내용2");

        PageRequest pageRequest = PageRequest.of(0, 6);

        Page<PostsResponse> rawPostsByKeyword = postsRepository.getPostsByKeyword(pageRequest, "고양이");
        List<PostsResponse> postsGroup = rawPostsByKeyword.stream().toList();
        assertThat(postsGroup.get(0).getContent()).isEqualTo("내용2");
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
