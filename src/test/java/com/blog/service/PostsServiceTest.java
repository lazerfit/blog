package com.blog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.domain.posts.Posts;
import com.blog.domain.posts.PostsEditor;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.PostsNotFound;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class PostsServiceTest {

    @Autowired
    private PostsRepository postsRepository;

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
        Posts posts = postsRepository.save(Posts.builder()
            .title("제목")
            .content("내용")
            .build());

        PostsUpdateRequestDto request = PostsUpdateRequestDto.builder()
            .title("수정된 제목")
            .content("수정된 내용")
            .build();

        PostsEditor.PostsEditorBuilder postsEditorBuilder = posts.toEditor();
        PostsEditor postsEditor = postsEditorBuilder
            .content(request.content())
            .build();

        posts.edit(postsEditor);

        assertThat(posts.getTitle()).isEqualTo("제목");
        assertThat(posts.getContent()).isEqualTo("수정된 내용");
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

        List<PostsResponseDto> postsList = postsRepository.getPostsList(request).stream().toList();

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
        Assertions.assertEquals("존재하지 않는 글입니다.",ex.getMessage());
    }
}
