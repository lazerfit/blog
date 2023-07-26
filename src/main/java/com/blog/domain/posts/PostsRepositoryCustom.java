package com.blog.domain.posts;

import com.blog.web.dto.posts.PostsResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsRepositoryCustom {
    Page<PostsResponse> fetchPostsExcludingComment(Pageable pageable);
    Page<PostsResponse> findPostsByKeyword(Pageable pageable,String keyword);
    Page<PostsResponse> findPostsSortedByCategory(Pageable pageable,String category);
    Page<PostsResponse> findPostsByTag(Pageable pageable,String tag);
    List<PostsResponse> getPopularPosts();
    List<PostsResponse> findPostsSortedByCategory(String q);
    // Using When go into Posts Detail page
    PostsResponse findPostsByIdIncludingComments(Long postId);
    Optional<PostsResponse> findPostById(Long id);
}
