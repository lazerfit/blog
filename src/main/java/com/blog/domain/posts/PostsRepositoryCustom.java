package com.blog.domain.posts;

import com.blog.web.dto.posts.PostsResponse;
import com.blog.web.dto.posts.PostsUpdateRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsRepositoryCustom {
    Page<PostsResponse> fetchPostsExcludingComment(Pageable pageable);
    Page<PostsResponse> findPostsByKeyword(Pageable pageable,String keyword);
    Page<PostsResponse> fetchPostsSortedByCategory(Pageable pageable,String category);
    Page<PostsResponse> findPostsByTag(Pageable pageable,String tag);
    List<PostsResponse> getPopularPosts();
    List<PostsResponse> fetchPostsSortedByCategory(String q);
    void edit(Long id, PostsUpdateRequest requestDto);

    // Using When go into Posts Detail page
    PostsResponse findPostsByIdIncludingComments(Long postId);

    Optional<PostsResponse> findPostsById(Long id);
}
