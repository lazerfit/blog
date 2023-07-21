package com.blog.domain.posts;

import com.blog.web.dto.PostsResponse;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsResponseWithoutComment;
import com.blog.web.dto.PostsUpdateRequestDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsRepositoryCustom {
    Page<PostsResponseWithoutComment> fetchPostsExcludingComment(Pageable pageable);
    Page<PostsResponse> findPostsByKeyword(Pageable pageable,String keyword);
    PostsResponseWithCategoryDto findByIdContainCategory(Long id);
    Page<PostsResponseWithCategoryDto> fetchPostsSortedByCategory(Pageable pageable,String category);
    Page<PostsResponseWithCategoryDto> getPostsByTags(Pageable pageable,String tag);
    List<PostsResponseWithoutComment> getPopularPosts();
    List<PostsResponse> fetchPostsSortedByCategory(String q);
    void edit(Long id, PostsUpdateRequestDto requestDto);

    // Using When go into Posts Detail page
    PostsResponse findByIdWithQdsl(Long postId);

    Optional<PostsResponse> findPostsById(Long id);
}
