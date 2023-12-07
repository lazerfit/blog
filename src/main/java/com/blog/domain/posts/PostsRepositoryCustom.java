package com.blog.domain.posts;

import com.blog.web.dto.posts.PostsResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsRepositoryCustom {
    Page<PostsResponse> getPosts(Pageable pageable);
    Page<PostsResponse> getPostsByKeyword(Pageable pageable,String keyword);
    Page<PostsResponse> getPostsSortedByCategory(Pageable pageable,String category);
    Page<PostsResponse> getPostsByTag(Pageable pageable,String tag);
    List<PostsResponse> getPopularPosts();
    List<PostsResponse> getPostsSortedByCategory(String q);
    // Using When go into Posts Detail page
    Optional<PostsResponse> getPostById(Long id);
}
